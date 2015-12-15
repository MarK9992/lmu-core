package fr.unice.polytech.retro.handler;

import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

public class GenerateHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getSelection();
				if (!(selection instanceof StructuredSelection)) return null;
				
				String res = "";
				for(Object o : ((StructuredSelection)selection).toArray()){
					res += o.toString() + " ";
				}
				
				JOptionPane.showMessageDialog(null, res + "\n");
				
		
		return 0;
	}

}
