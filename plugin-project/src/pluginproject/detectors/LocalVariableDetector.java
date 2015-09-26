package pluginproject.detectors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class LocalVariableDetector extends ASTVisitor {
	
	private List<String> result = new ArrayList<String>();
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		for (Iterator<?> iter = node.fragments().iterator(); iter.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
			IVariableBinding binding = fragment.resolveBinding();
			
			result.add("LOCAL VARIABLE VISIT: " + binding.getType().getName() + " " + fragment.getName());
		}
		return super.visit(node);
	}
	
	public List<String> getResult() {
		return result;
	}
}
