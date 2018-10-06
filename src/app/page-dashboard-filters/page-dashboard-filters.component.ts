import {Component, OnDestroy} from '@angular/core';
import {SharedHazelcastAgentService} from '@shared/services/shared-hazelcast-agent.service';
import {SharedSnackbarService} from '@shared/services/shared-snackbar.service';
import {TabAwareComponent, TabData} from '@shared/components/dynamic-tabs/shared-dynamic-tabs.model';
import {CompileFiltersRequestDTO, CompileFiltersResponseDTO, ErrorMessageDTO} from '@shared/dto/hazelcast-monitor.dto';
import ICodeEditor = monaco.editor.ICodeEditor;

@Component({
  templateUrl: './page-dashboard-filters.component.html',
  styleUrls: [ './page-dashboard-filters.component.scss' ]
})
export class PageDashboardFiltersComponent implements TabAwareComponent, OnDestroy {
  public editorOptions: any = {
    theme: 'vs',
    language: 'java',
    automaticLayout: true,
    minimap: {
      enabled: false
    }
  };

  public code: string = `import it.xdnl.hazelcast.monitor.agent.filter.ICollectionFilter;
import it.xdnl.hazelcast.monitor.agent.filter.IMapFilter;

public class PassFilter implements ICollectionFilter, IMapFilter {
  @Override
  public boolean matches(final Object element) {
    return true;
  }
  
  @Override
  public boolean matches(final Object key, final Object value) {
    return true;
  }
} 

public class NoneFilter implements ICollectionFilter, IMapFilter {
  @Override
  public boolean matches(final Object element) {
    return false;
  }
  
  @Override
  public boolean matches(final Object key, final Object value) {
    return false;
  }
} 
`;

  public constructor(private snackbarService: SharedSnackbarService,
                     private hazelcastService: SharedHazelcastAgentService) {
  }

  public ngOnDestroy(): void {
    this.beforeHide();
  }

  public compile(): void {
    const request: CompileFiltersRequestDTO = {
      messageType: 'compile_filters',
      source: this.code
    };

    this.hazelcastService.sendCompile(request).then((response: CompileFiltersResponseDTO) => {
      if (!!response.errors) {
        this.snackbarService.show(`Compile error: ${(response as CompileFiltersResponseDTO).errors}`);
      } else {
        this.snackbarService.show('Compile success');
      }
    }).catch((error: ErrorMessageDTO) => {
      this.snackbarService.show(`Compile error: ${error.errors.join('\n')}`);
    });
  }

  public onEditorInitialized(editor: ICodeEditor): void {
    this.patchFocusTrap(editor.getDomNode());
  }

  /**
   * Prevents the TAB key to be handled by the focus trap created automatically by mdc-dialog.
   *
   *    // Focus trap in dialog.es5.js
   *    // handleTab is invoked in the capturing phase!
   *    function handleTab(e) {
   *         updateTabbableNodes();
   *
   *         if (e.target.hasAttribute('tabindex') && Number(e.target.getAttribute('tabindex')) < 0) {
   *           return tabEvent = e;
   *         }
   *
   *         e.preventDefault();
   *         ...
   *     }
   *
   * @param {HTMLElement} editorDomNode
   */
  private patchFocusTrap(editorDomNode: HTMLElement): void {
    const inputarea: HTMLTextAreaElement = editorDomNode.querySelector('textarea.inputarea');
    if (!!inputarea) {
      inputarea.setAttribute('tabindex', '-1');
      console.info('ngx-monaco-editor focustrap patch applied');
    } else {
      console.warn('ngx-monaco-editor focustrap patch failed: input area not found~');
    }
  }

  public beforeShow(): void {
  }

  public beforeHide(): void {
  }

  public tabCreated(tab: TabData): void {
  }
}
