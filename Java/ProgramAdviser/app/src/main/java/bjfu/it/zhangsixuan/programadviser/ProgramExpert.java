package bjfu.it.zhangsixuan.programadviser;

public class ProgramExpert {
    public String getLanguage(String feature) {
        String res;
        switch (feature) {
            case "fast":
                res = "C/C++";
                break;
            case "easy":
                res = "Python";
                break;
            case "new":
                res = "Kotlin";
                break;
            case "OO":
                res = "Java";
                break;
            default:
                res = "U got me";
        }
        return res;
    }
}
