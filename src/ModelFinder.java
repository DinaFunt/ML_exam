import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ModelFinder {

    private Sequent start;
    private boolean flag;
    private StringBuilder sb;
    private ArrayList<String> props;


    public ModelFinder(String f) {
        flag = false;

        props = findProps(f);
        ArrayList<String> s = new ArrayList<String>();
//вводим начальную формулу в сукцедент
        s.add(f.trim());

        start = new Sequent(new ArrayList<String>(), new ArrayList<>(s));

        sb = new StringBuilder();
    }

    private ArrayList<String> findProps(String s) {
        String[] res = s.split("[^a-z]");
        ArrayList<String> p = new ArrayList<String>();

        for (String str : res) {
            if (!str.equals("")) {
                p.add(str);
            }
        }

        return p;
    }


    private void createTree(Sequent tn) {
        String x;
        Iterator<String> i = tn.getAntecedent().iterator();

        while (i.hasNext()) {
            x = i.next();
            if (isFormula(x)) {
                antIntro(x, tn);
                return;
            }
        }

        Iterator<String> j = tn.getSuccedens().iterator();

        while (j.hasNext()) {
            x = j.next();
            if (isFormula(x)) {
                succIntro(x, tn);
                return;
            }
        }

        if (!findMatches(tn.getAntecedent(), tn.getSuccedens())) {
            sb.append('\"').append(tn.getStringForm()).append('\"').
                    append("[style=\"filled\",fillcolor=\"blue\"];\n");
            if (!flag) {
                flag = true;
                printCountr(tn.getAntecedent(), tn.getSuccedens());
            }
        }

    }

    public String[] parse(String str, String op) {
        str = removeParentheses(str);
        int pk = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i).startsWith(op) && pk ==  0) {
                return new String[]{str.substring(0, i).trim(), str.substring(i + op.length()).trim()};
            }

            if (str.charAt(i) == '(') {
                pk++;
            }

            if (str.charAt(i) == ')') {
                pk--;
            }
        }

        return new String[]{str.trim()};
    }

    private String removeParentheses(String str) {
        String result;
        if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')') {
            result = str.substring(1, str.length() - 1);

            int pk = 0;

            for (int i = 0; i < result.length(); i++) {
                if (result.charAt(i) == '(') {
                    pk++;
                }

                if (result.charAt(i) == ')') {
                    pk--;
                }

                if (pk < 0) {
                    return str;
                }
            }

            if (pk == 0) {
                return removeParentheses(result);
            }

        } else {
            return str;
        }

        return str;
    }

    private void antIntro(String f, Sequent tn) {
        String[] res;

        if ((res = parse(f, "IMPL")).length != 1) {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            a.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));
            Sequent s2 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));
            s1.addSucc(res[0].trim());
            s2.addAnt(res[1].trim());

            addToGraph(str, s1.getStringForm());
            addToGraph(str, s2.getStringForm());

            createTree(s1);
            createTree(s2);
        }

        else if ((res = parse(f, "OR")).length != 1) {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            a.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));
            Sequent s2 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));
            s1.addAnt(res[0].trim());
            s2.addAnt(res[1].trim());

            addToGraph(str, s1.getStringForm());
            addToGraph(str, s2.getStringForm());

            createTree(s1);
            createTree(s2);
        }
        else if ((res = parse(f, "AND")).length != 1) {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            a.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));

            s1.addAnt(res[0].trim());
            s1.addAnt(res[1].trim());

            addToGraph(str, s1.getStringForm());

            createTree(s1);
        }
        else {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            a.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));

            f = removeParentheses(f);
            s1.addSucc(f.substring(f.indexOf("NOT") + 3).trim());

            addToGraph(str, s1.getStringForm());
            createTree(s1);

        }


    }

    private void addToGraph(String s1, String s2) {
        sb.append('"').append(s1).append('"').append(" -> ").append('"').append(s2).append("\"\n");
    }

    private void succIntro(String f, Sequent tn) {
        String[] res;

        if ((res = parse(f, "IMPL")).length != 1) {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            s.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));

            s1.addAnt(res[0].trim());
            s1.addSucc(res[1].trim());

            addToGraph(str, s1.getStringForm());

            createTree(s1);
        }

        else if ((res = parse(f, "OR")).length != 1) {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            s.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));

            s1.addSucc(res[0].trim());
            s1.addSucc(res[1].trim());

            addToGraph(str, s1.getStringForm());

            createTree(s1);
        }

        else if ((res = parse(f, "AND")).length != 1) {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            s.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));
            Sequent s2 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));
            s1.addSucc(res[0].trim());
            s2.addSucc(res[1].trim());

            addToGraph(str, s1.getStringForm());
            addToGraph(str, s2.getStringForm());

            createTree(s1);
            createTree(s2);
        } else {
            ArrayList<String> a = tn.getAntecedent();
            ArrayList<String> s = tn.getSuccedens();

            String str = tn.getStringForm();
            s.remove(f);

            Sequent s1 = new Sequent(new ArrayList<>(a), new ArrayList<>(s));

            f = removeParentheses(f);
            s1.addAnt(f.substring(f.indexOf("NOT") + 3).trim());

            addToGraph(str, s1.getStringForm());
            createTree(s1);

        }

    }

    private boolean findMatches(ArrayList<String> a, ArrayList<String> s) {

        for (String elem : a) {
            if(s.contains(elem)) {
                return true;
            }
        }

        return false;
    }

    private boolean isFormula(String f) {
        String[] rAnd = f.split(" AND ", 2);
        String[] rOR = f.split(" OR ", 2);
        String[] rIMPL = f.split(" IMPL ", 2);

        return  (f.contains("NOT") || rAnd.length != 1 || rOR.length != 1 || rIMPL.length != 1);
    }


    public void printCountr(ArrayList<String> a, ArrayList<String> s) {
        for (String str : props) {
            if (a.contains(str)) {
                System.out.println(str + " = " + "True");
            } else {
                System.out.println(str + " = " + "False");
            }
        }
    }

    public boolean countrExist() {
        sb.append("digraph {\n");
        createTree(start);
        sb.append('}');

        if (!flag) {
            System.out.println("Countr not found");
        }
        try {
            FileWriter writer = new FileWriter(new File("result.dot"));

            writer.write(sb.toString());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        try {
            Runtime.getRuntime().exec("dot -Tpng result.dot -o res.png");
        } catch (IOException e) {
            System.out.println("Graphviz is not installed!");
        }
        return flag;
    }
}
