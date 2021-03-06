package wechesspresso;
import chesspresso.position.*;
import chesspresso.move.*;
import java.util.ArrayList;
import java.util.Scanner;



public class WeChesspresso {
    
    
    
    public static void printAllMoves(String fen){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        System.out.println("Possible Moves : ");
        
        for(int i=0;i<allMoves.length;i++){
            System.out.println(Move.getString(allMoves[i]));
        }
    }
    
    public static void printAllMovesVerbose(String fen){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        System.out.println("Possible Moves : ");
        
        for(int i=0;i<allMoves.length;i++){
            System.out.print(Move.getString(allMoves[i])+"  ");
            System.out.print(allMoves[i]);
            if(Move.isEPMove(allMoves[i])){
                System.out.print("  En Passant Move");
            }
            else if(Move.isCapturing(allMoves[i])){
                System.out.print("  Capturing Move");
            }
            else if(Move.isShortCastle(allMoves[i])){
                System.out.print("  Short Castle Move");
            }
            else if(Move.isLongCastle(allMoves[i])){
                System.out.print("  Long Castle Move");
            }
            else if(Move.isPromotion(allMoves[i])){
                System.out.print("  Promotion Move");
            }
            else{
                System.out.print("  Normal Move");
            }
            System.out.println("");
        }
    }
    
    public static boolean isCheck(String fen){
        Position pos=new Position(fen);
        return pos.isCheck();
    }
    
    public static boolean isPat(String fen){
        Position pos=new Position(fen);
        return pos.isStaleMate();
    }
    
    public static boolean isCheckMate(String fen){
        Position pos=new Position(fen);
        return pos.isMate();
    }
    
    public static boolean isShortCastlePossible(String fen){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(move.equals("0-0")){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isLongCastlePossible(String fen){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(move.equals("0-0-0")){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isMoveCapturing(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            System.out.println(move);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isCapturing(allMoves[i])){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isMoveEP(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isEPMove(allMoves[i])){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isMoveShortCastle(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isShortCastle(allMoves[i])){
                    return true;
                }
            }
        }
        return false;
    }
    
    
    
    public static boolean isMoveLongCastle(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isLongCastle(allMoves[i])){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isMovePromotion(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isPromotion(allMoves[i])){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isMoveValid(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isValid(allMoves[i])){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static short getShortMoveFromPos(String fen, String pos1, String pos2){
        Position pos=new Position(fen);
        short[] allMoves;
        allMoves=pos.getAllMoves();
        String move;
        for(int i=0;i<allMoves.length;i++){
            move=Move.getString(allMoves[i]);
            if(pos1.equals(move.substring(0,2)) && pos2.equals(move.substring(move.length()-2,move.length()))){
                if(Move.isValid(allMoves[i])){
                    return allMoves[i];
                }
            }
        }
        return 0;
    }
    
    public static String getMoveType(String fen, String move){
        Position pos=new Position(fen);
        String pos1 = move.substring(0,2);
        String pos2 = move.substring(move.length()-2,move.length());
        if(isMoveEP(fen,pos1,pos2)){
            return "ep";
        }
        else if(isMoveLongCastle(fen,pos1,pos2)){
            return "groc";
        }
        else if(isMoveShortCastle(fen,pos1,pos2)){
            return "proc";
        }
        else if(isMovePromotion(fen,pos1,pos2)){
            return "prom";
        }
        else if(isMoveCapturing(fen,pos1,pos2)){
            return "capt";
        }
        else{
            return "norm";
        }
    }
    
        public static String getMoveType(String fen, String pos1, String pos2){
        if(isMoveEP(fen,pos1,pos2)){
            return "ep";
        }
        else if(isMoveLongCastle(fen,pos1,pos2)){
            return "groc";
        }
        else if(isMoveShortCastle(fen,pos1,pos2)){
            return "proc";
        }
        else if(isMovePromotion(fen,pos1,pos2)){
            return "prom";
        }
        else if(isMoveCapturing(fen,pos1,pos2)){
            return "capt";
        }
        else{
            return "norm";
        }
    }
    
    public static String getNewFen(String fen, String pos1, String pos2) throws IllegalMoveException{
        Position pos=new Position(fen);
        if(isMoveValid(fen,pos1,pos2)){
            pos.doMove(getShortMoveFromPos(fen,pos1,pos2));
            return pos.getFEN();
        }
        return "no";
    }
    
    public static String getNewFen(String fen, String move) throws IllegalMoveException{
        Position pos=new Position(fen);
        String pos1 = move.substring(0,2);
        String pos2 = move.substring(move.length()-2,move.length());
        System.out.println("[GETNEWFEN ] fen : "+fen);
        System.out.println("[GETNEWFEN ] pos1 : "+pos1);
        System.out.println("[GETNEWFEN ] pos2 : "+pos2);
        if(isMoveValid(fen,pos1,pos2)){
            
            pos.doMove(getShortMoveFromPos(fen,pos1,pos2));
            return pos.getFEN();
        }
        else throw new IllegalMoveException(move);
    }
    
    static void printFEN(String fen){
        String[] alines = fen.split("/");
        ArrayList<String> lines = new ArrayList();
        
        for(int i=0;i<alines.length;i++){
            lines.add(alines[i]);
        }
        String space = new String("");
        int lig=8;
        System.out.println(" |abcdefgh|");
        //System.out.println("------------");
        for (String s : lines) {
            space = "";
            System.out.print(lig+"|");
            for (int c = 1; c <= 8; c++) {
                space=space.concat(".");
                s = s.replace(String.valueOf(c), space);
            }
            
            s = s.substring(0, 8);
            
            System.out.print(s);
            System.out.println("|"+lig--);
        }
        
        //System.out.println("------------");
        System.out.println(" |abcdefgh|");
    }
    
    // TESTS --- Jouer alone
    public static void main(String[] args) throws IllegalMoveException {
        //FEN de départ -> rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        //FEN avec en passant ready -> rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3
        //FEN avec short castle ready -> 2bqk2r/1pppb1p1/r3pP2/p6p/PP3PnP/3P4/3PB1P1/RNBQK1NR b KQk - 2 13
        //FEN avec long castle ready -> 
        // Short Castle move 0-0
        // Long Castle move 0-0-0
        // En passant move <-> capture
        String fen="rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3";
        String pos1,pos2,newfen,move="";
        Scanner input = new Scanner(System.in);
        while(!move.equals("q")){
            printFEN(fen);
            System.out.println("FEN :"+fen);
            WeChesspresso.printAllMovesVerbose(fen);
            System.out.println("Your move: ");
            move=input.nextLine();
            pos1=move.substring(0,2);
            pos2=move.substring(move.length()-2,move.length());
            if(isMoveValid(fen,pos1,pos2)){
                fen=getNewFen(fen,pos1,pos2);
            }
            else{
                System.out.println("bad mthfckr");
            }
        }
    }
}
