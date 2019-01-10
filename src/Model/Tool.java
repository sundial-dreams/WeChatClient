package Model;

public class Tool {
    public static double getWidth(String Msg){

        int len = Msg.length();
        double width=20;
        for(int i=0;i<len;i++)
        {
            if(isChinese(Msg.charAt(i))){
                width+=17;

            }
            else
            {
                width+=9;
            }
        }
        //29中 17=15px/64英 8=7px
        if(width<=480)
        {
            return width;
        }
        else
        {
            return 480;
        }
    }
    public static double getHight(String Msg){
        int len = Msg.length();
        double width = 20;
        double height = 40;
        for(int i=0;i<len;i++){

            if(isChinese(Msg.charAt(i))){
                width+=17;
            }
            else
            {
                width+=9;
            }
            if(width>=480)
            {
                height+=17.4;
                width=20;
            }

        }
        return height;

    }
    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
