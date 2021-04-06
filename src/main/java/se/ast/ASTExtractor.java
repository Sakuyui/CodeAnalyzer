package se.ast;

import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import se.lexer.Java8Lexer;
import se.lexer.Java8Parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.jdt.core.dom.*;
import spoon.reflect.declaration.CtElement;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ASTExtractor {

    public static String readFile() throws IOException {
        String filePath = "D:\\storage\\upload\\user\\405544641\\JavaA\\DataStructures\\Graphs\\A_Star.java";
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        return new String(input);
    }



    public static String GetTokenizedSymbolicsNames(String codeSnippet){
        Java8Lexer lexer = new Java8Lexer(new ANTLRInputStream(codeSnippet));
        List<? extends Token> tokenList = lexer.getAllTokens();
        String res = "";
        for(Token token : tokenList){
            res += lexer.getVocabulary().getSymbolicName(token.getType()) + " ";
        }
        return res;
    }

    public static void WriteTokensTo(String code, String path) throws IOException {
        Java8Lexer lexer = new Java8Lexer(new ANTLRInputStream(code));
        List<? extends Token> tokenList = lexer.getAllTokens();
        String res = "";
        File f = new File(path);
        if(f.exists()) {
            f.delete();
        }
        FileWriter writer = new FileWriter(f);
        for(Token token : tokenList){
            writer.write(lexer.getVocabulary().getSymbolicName(token.getType()) + " " + token.getText().replaceAll(" ", "") + "\n");
        }
        writer.close();
    }


    public static void printAST(RuleContext ctx, boolean verbose, int indentation) {
        boolean toBeIgnored = !verbose && ctx.getChildCount() == 1 && ctx.getChild(0) instanceof ParserRuleContext;


        if (!toBeIgnored) {
            String ruleName = Java8Parser.ruleNames[ctx.getRuleIndex()];
            for (int i = 0; i < indentation; i++) {
                System.out.print("--");
            }
            System.out.print("("+indentation+") ");
            System.out.println("[" + ruleName + "]" + " -> " + ctx.getText());
        }
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                printAST((RuleContext) element, verbose, indentation + (toBeIgnored ? 0 : 1));
            }
        }
    }

    public static JSONObject CreateASTStr(RuleContext ctx, boolean verbose, int indentation) {
        boolean toBeIgnored = !verbose && ctx.getChildCount() == 1 && ctx.getChild(0) instanceof ParserRuleContext;
        String ruleName = Java8Parser.ruleNames[ctx.getRuleIndex()];
        //System.out.println(ruleName);
        if (!toBeIgnored) {

            for (int i = 0; i < indentation; i++) {
               // System.out.print("--");
            }
            System.out.print("("+indentation+") ");
            System.out.println("[" + ruleName + "]" );
        }
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("text", ruleName);

        List<String> subTreeStr = new ArrayList<>();
        JSONArray array = new JSONArray();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                JSONObject t = CreateASTStr((RuleContext) element, verbose, indentation + (toBeIgnored ? 0 : 1));
                if(t!=null){
                    array.add(t);
                }
            }
        }
        jsonObject.put("children", array);

        return jsonObject;
    }

    public static HashSet<String> pruneNodeType = getPruneNodeTypeSet();
    public static HashSet<String> getPruneNodeTypeSet(){
        HashSet<String> tabooSet = new HashSet<>();
        tabooSet.add("typeImportOnDemandDeclaration");
        return tabooSet;
    }
    public static void dfsANTLRAst(RuleContext ctx, boolean verbose, int d, StringBuilder astString, StringBuilder serializedString){
        boolean toBeIgnored = !verbose && ctx.getChildCount() == 1 && ctx.getChild(0) instanceof ParserRuleContext;

        String ruleName = Java8Parser.ruleNames[ctx.getRuleIndex()];
        //System.out.println(ruleName);
        if (!toBeIgnored) {

            for (int i = 0; i < d; i++) {
                // System.out.print("--");
            }
            astString.append("(" + ruleName);
            System.out.print("(" + d + ") " + repeatString("-", d) + " ");
            serializedString.append(ruleName + " ");
            System.out.println("[" + ruleName + "]" );
        }


        List<String> subTreeStr = new ArrayList<>();

        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree element = ctx.getChild(i);
            if (element instanceof RuleContext) {
                if(pruneNodeType.contains(Java8Parser.ruleNames[((RuleContext) element).getRuleIndex()]))
                    continue;
                dfsANTLRAst((RuleContext) element, verbose, d + (toBeIgnored ? 0 : 1), astString, serializedString);

            }

        }
        if(!toBeIgnored)
        {
            astString.append(")");
            //System.out.println("+)");
        }


    }
    public static void dfs(CtElement element, int d){
        System.out.println("dfs ("+d +") >>: " +" type = " + repeatString("-", d) + " " + element.getRoleInParent());
        List<CtElement> elements = element.getDirectChildren();
        for(CtElement e: elements){
            dfs(e, d + 1);
        }
    }
    public static String repeatString(String s, int times){
        StringBuilder sb = new StringBuilder("");
        for(var i = 0; i < times; i++){
            sb.append(s);
        }
        return sb.toString();
    }

    public static String readFullFile(String filePath) throws IOException{

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        return new String(input);
    }



}


class ClassB{
    int v = 0;
    public int funcA(int v){
        v += 2;
        return v;
    }
    public void funcB(){
        v += 2;
    }
}