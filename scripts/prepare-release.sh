#!/usr/bin/env bash

# Check that we are in the root folder
if ! [[ -f "./scripts/prepare-release.sh" ]]; then
    (>&2 echo "Script must be run from the root folder, actual $(pwd)")
    exit 1
fi

# Get the current branch
branch_name=$(git symbolic-ref -q HEAD)
branch_name=${branch_name##refs/heads/}
branch_name=${branch_name:-HEAD}

# Check that we are in master
if [[ ${branch_name} != 'master' ]]; then
    (>&2 echo "Releases can be prepared only on master branch, current one is ${branch_name}!")
    exit 1
fi

# Check the arguments
if [[ "$#" -ne 1 ]]; then
    (>&2 echo "Invalid number of arguments, found $#, expected 1")
    echo "prepare-release <new-version>"
    exit 1
fi

# Fetch (for picking tags)
git fetch
existing_tag=$(git tag --list | grep $1 | wc -l)
if [[ ${existing_tag} -gt 0 ]]; then
    (>&2 echo "Tag $1 already exists, stop.")
    exit 1
fi

# Merge
echo "Merging from dev"
git merge origin/dev --no-ff --no-commit

# Check if there are any conflicts
conflicts=$(git ls-files -u | wc -l)
if [[ ${conflicts} -gt 0 ]]; then
   (>&2 echo "There are merge conflicts, stop (use git merge --abort to undo changes).")
   exit 1
fi

# Update the version
echo "Setting project version to $1"
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$1

# Compile with tests
mvn clean verify

if [[ $? -ne 0 ]]; then
    (>&2 echo "Project build failed, stop (use git merge --abort to undo changes).")
    exit 1
fi

# Success: commit and tag
echo "Build success, creating tagged commit"
git commit -am "chore: version $1"
git tag -a $1 -m "$1"

# Ready to push!
# git push origin master --tags