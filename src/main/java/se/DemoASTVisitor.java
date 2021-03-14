package se;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class DemoASTVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

    @Override
    public boolean visit(FieldDeclaration node) {
        for (Object obj: node.fragments()) {
            VariableDeclarationFragment v = (VariableDeclarationFragment)obj;
            System.out.println("Field:\t" + v.getName());
        }

        return true;
    }

    @Override
    public void preVisit(ASTNode node) {
        System.out.println(node.getAST());
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        System.out.println("Method:\t" + node.getName());
        return true;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        System.out.println("Class:\t" + node.getName());
        return true;
    }
}