package se;
import se.ast.ASTExtractor;
import com.alibaba.fastjson.JSONObject;
import se.lexer.Java8Lexer;
import se.lexer.Java8Parser;
import network.SocketClient;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import spoon.Launcher;
import spoon.reflect.declaration.CtElement;

import java.io.IOException;
import java.util.List;
import static se.ast.ASTExtractor.GetTokenizedSymbolicsNames;
import static se.ast.ASTExtractor.dfsANTLRAst;

public class ASTRun {
    public static void main(String[] args){
        String filePath = "D:\\storage\\upload\\user\\405544641\\JavaA\\DataStructures\\Graphs\\A_Star.java";
        Launcher spoon = new Launcher();

        String code =
                "import java.io.*;\n" +
                "class ClassB {\n" +
                "    int v = 0;  //6\n" +
                "    public int funcA(int v) { //6\n" +
                "        v += 2; //7\n" +
                "        return v;\n" +
                "    }\n" +
                "    public void funcB() {\n" +
                "        v += 2; //8\n" +
                "    }\n" +
                "}\n";


        //词条序列部分
        CtElement l = spoon.parseClass(code);

        ANTLRInputStream in = new ANTLRInputStream(code); //antlr
        Java8Lexer lexer = new Java8Lexer(in);
        List<? extends Token> tokenList = lexer.getAllTokens();
        for(Token token : tokenList){
            System.out.println("Next token :" + token.getText() +" >> " +
                    " " + token.getType()  + " " + lexer.getVocabulary().getSymbolicName(token.getType()) + "\n");
        }

        System.out.println(GetTokenizedSymbolicsNames(code));

        //词条序列部分


        //AST部分
        //ASTExtractor.dfs(l,0);
        ANTLRInputStream input = new ANTLRInputStream(code);
        lexer = new Java8Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        Java8Parser parser = new Java8Parser(tokens);
        ParserRuleContext ctx = parser.compilationUnit();
        StringBuilder ans = new StringBuilder("");
        StringBuilder serialString = new StringBuilder("");
        dfsANTLRAst(ctx, false, 0, ans, serialString);
        System.out.println(ans);
        System.out.println(serialString);
        //JSONObject t1 = ASTExtractor.CreateASTStr(ctx, false, 0);
        //System.out.println(t1);
        //WriteTokensTo(code, "a.csv");
        //ASTExtractor.printAST(ctx,false,0);
        //System.out.println(t1.toJSONString());

        if(true)
            return;

        SocketClient socketClient = new SocketClient("127.0.0.1", 2448);
        socketClient.sendMessage("<Type>101</><value>java</>");




    }
}
