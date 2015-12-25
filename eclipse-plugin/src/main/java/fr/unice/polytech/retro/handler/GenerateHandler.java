package fr.unice.polytech.retro.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;


public class GenerateHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getSelection();
				if (!(selection instanceof StructuredSelection)) return null;

				Set<String> res = new HashSet<>();
				StructuredSelection sSelection = (StructuredSelection) selection;
				
				for(Object o : sSelection.toArray()){
					System.out.println("hfdsfshqdfs !: " + o.getClass().getCanonicalName());
					if(o.getClass().getCanonicalName().endsWith("CompilationUnit")){
						res.add(treatJavaFile((ICompilationUnit) o));
					} else if(o.getClass().getCanonicalName().endsWith("PackageFragment")){
						try {
							res.add(treatPackage((IPackageFragment) o));
						} catch (JavaModelException e) {
							e.printStackTrace();
						}
					} else if(o.getClass().getCanonicalName().endsWith("PackageFragmentRoot")){
						res.add(treatPackageRoot((IPackageFragmentRoot) o));
					} else if(o.getClass().getCanonicalName().endsWith("Project")){
						res.add(treatProject((IProject) o));
					}	
				}
				
				for(String el : res){
					Controller.analyzeElement(res);
				}
				JOptionPane.showMessageDialog(null, res + "\n");
				
		
		return 0;
	}

	private String treatProject(IProject ip) {
		return "/" + ip.getName();
	}

	private String treatPackageRoot(IPackageFragmentRoot ipfr) {
		IPath path = ipfr.getPath();
		return path.toPortableString();
	}

	private String treatPackage(IPackageFragment ipf) throws JavaModelException {
		IPath path = ipf.getPath();
		return path.toPortableString();
	}

	private String treatJavaFile(ICompilationUnit icu) {
		IPath path = icu.getPath();
		return path.toPortableString();
	}

}
