#!/usr/bin/env bash

# Check that we are in the root folder
if ! [[ -f "./scripts/start-next-iteration.sh" ]]; then
    (>&2 echo "Script must be run from the root folder, actual $(pwd)")
    exit 1
fi

# Get the current branch
branch_name=$(git symbolic-ref -q HEAD)
branch_name=${branch_name##refs/heads/}
branch_name=${branch_name:-HEAD}

# Check that we are in dev
if [[ ${branch_name} != 'dev' ]]; then
    (>&2 echo "Next iterations can be started only on dev branch, current one is ${branch_name}!")
    exit 1
fi

# Check the arguments
if [[ "$#" -ne 1 ]]; then
    (>&2 echo "Invalid number of arguments, found $#, expected 1")
    echo "start-next-iteration <new-version>"
    exit 1
fi

# Check that the working tree is clean
if [[ -n $(git status -s) ]]; then
    (>&2 echo "Working tree is not clean, stop")
    exit 1
fi

# Update
git fetch

# Try to fast forward the dev branch
git merge origin/dev --ff-only

# Check for no conflicts
if [[ $? -ne 0 ]]; then
    (>&2 echo "There are remote changes in dev branch that could not be merged automatically, stop.")
    exit 1
fi

# Change the version
echo "Setting project version to $1"
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$1

# Success: commit
echo "Success, creating commit"
git commit -am "chore: start next development iteration ($1)"

# Ready to push!
#git push origin dev