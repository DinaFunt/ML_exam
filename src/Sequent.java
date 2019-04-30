import java.util.ArrayList;

public class Sequent {
    private ArrayList<String> antecedent;
    private ArrayList<String> succedent;

   // protected Sequent left;
    //protected Sequent right;
    //protected Sequent head;


    public Sequent(ArrayList<String> a, ArrayList<String> s) {
        antecedent = a;
        succedent = s;
    }

    public ArrayList<String> getAntecedent() {
        return antecedent;
    }

    public ArrayList<String> getSuccedent() {
        return succedent;
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

        int turnstile = 0x22A2;
        sb.append((char) turnstile).append(' ');

        for (String t : succedent) {
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
        succedent.add(s);
    }
}
