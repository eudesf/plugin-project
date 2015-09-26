package pluginproject.popup.actions;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import pluginproject.detectors.LocalVariableDetector;

public class PluginAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection selection;
	
	/**
	 * Constructor for Action1.
	 */
	public PluginAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		CompilationUnit selection = processSelection(); 
		if (selection != null) {
			LocalVariableDetector variableVisitor = new LocalVariableDetector();
			selection.accept(variableVisitor);
			MessageDialog.openInformation(
					shell, "Plugin-project", "Local Variables:\n" + 
							variableVisitor.getResult().toString()
							.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "\n"));
		} else {
			MessageDialog.openInformation(
					shell, "Plugin-project", "Plugin Action was executed without selection.");
		}
	}

	private CompilationUnit processSelection() {
		if (selection instanceof IStructuredSelection) {
			return parse((ICompilationUnit) ((IStructuredSelection) selection).getFirstElement());
		}
		return null;
	}

	private CompilationUnit parse(ICompilationUnit lwUnit) {
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(lwUnit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
	}
	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
