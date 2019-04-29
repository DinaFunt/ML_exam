import java.util.ArrayList;

public class Sequent {
    private ArrayList<String> antecedent;
    private ArrayList<String> succedens;

   // protected Sequent left;
    //protected Sequent right;
    //protected Sequent head;


    public Sequent(ArrayList<String> a, ArrayList<String> s) {
        antecedent = a;
        succedens = s;
    }

    public ArrayList<String> getAntecedent() {
        return antecedent;
    }

    public ArrayList<String> getSuccedens() {
        return succedens;
    }

    public String getStringForm() {
        StringBuilder sb = new StringBuilder();

        for (String t : antecedent) {
            sb.append(t).append("; ");
        }

        int index = sb.lastIndexOf(";");
        if (index != -1) {
            sb.deleteCharAt(index);
        }

        int corkscrewCode = 0x22A2;
        sb.append((char) corkscrewCode).append(' ');

        for (String t : succedens) {
            sb.append(t).append("; ");
        }

        index = sb.lastIndexOf(";");
        if (index != -1) {
            sb.deleteCharAt(index);
        }

        return sb.toString().trim();
    }

    public void addAnt(String s) {
        antecedent.add(s);
    }

    public void addSucc(String s) {
        succedens.add(s);
    }
}
