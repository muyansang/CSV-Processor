package cs2110;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CsvJoin {
    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String file) throws IOException {
        assert file != null;

        FileReader content = new FileReader(new File(file));
        Scanner fileData = new Scanner(content);
        Seq<Seq<String>> fileRepresentation = new LinkedSeq<>();
        while (fileData.hasNextLine()) {
            String line = fileData.nextLine();
            String[] lineData=line.split(",",-1);

            Seq<String> row = new LinkedSeq<>();
            for (String singleData : lineData) {
                row.append(singleData);
            }
            fileRepresentation.append(row);
        }
        return fileRepresentation;
    }

    /**
     * Test a table to see whether it's rectangular. return a boolean value for it. REsult
     * will represent whether the table is a rectangular table.
     */
    private static boolean isRectangle(Seq<Seq<String>> table){
        int size=table.get(0).size();
        for (Seq<String> row : table) {
            if (row == null || row.size() != size){
                return false;
            }

        }
        return true;
    }

    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right) {
        assert left != null && right != null;
        assert (left.get(0).size() > 0) && (right.get(0).size() > 0);

        int rightSize = right.get(0).size();

        assert isRectangle(left);
        for (Seq<String> leftRow : left) {
            for (String singleItem : leftRow) {
                assert singleItem != null;
            }
        }
        assert isRectangle(right);

        for (Seq<String> rightRow : right) {
            for (String singleItem : rightRow) {
                assert singleItem != null;
            }
        }

        Seq<Seq<String>> mergeTable = new LinkedSeq<>();

        for (Seq<String> leftRow : left) {
            Seq<String> newRow = new LinkedSeq<>();
            Seq<String> store =new LinkedSeq<>();
            Boolean flag = false;


            for (String singleItem : leftRow) {
                newRow.append(singleItem);
                store.append(singleItem);
            }


            for (Seq<String> rightRow : right) {
                if (leftRow.get(0).equals(rightRow.get(0))) {
                    int i = 0;
                    for (String single : rightRow) {
                        if (i > 0){
                            newRow.append(single);
                        }
                        i++;
                    }
                    mergeTable.append(newRow);
                    newRow = store;
                    flag = true;
                }
            }

            if (!flag) {
                for (int i = 0; i < (rightSize - 1); i++) {
                    newRow.append("");
                }
                mergeTable.append(newRow);
            }
        }
        return mergeTable;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: cs2110.CsvJoin <left_table.csv> <right_table.csv>");
            System.exit(1);
        }

        if (args[0] == null || args[1] == null) {
            System.err.println("Usage: cs2110.CsvJoin <left_table.csv> <right_table.csv>");
            System.exit(1);
        }

        String file1 = args[0];
        String file2 = args[1];
        File f1 = new File(file1);
        File f2 = new File(file2);
        if (!f1.canRead()) {
            System.err.println("Error: Could not read input tables.");
            System.err.println("java.io.FileNotFoundException: " + file1 + "(No such file or directory)");
            System.exit(1);
        }
        if (!f2.canRead()) {
            System.err.println("Error: Could not read input tables.");
            System.err.println("java.io.FileNotFoundException: " + file2 + "(No such file or directory)");
            System.exit(1);
        }

        Seq<Seq<String>> list1 = null;
        Seq<Seq<String>> list2 = null;
        Seq<Seq<String>> mergedTable = null;


        try {
            list1 = csvToList(file1);
            list2 = csvToList(file2);
            if (!isRectangle(list1) || !isRectangle(list2)){
                System.err.println("Error: Input table not rectangular");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not convert input tables from csv to lists.");
            System.exit(1);
        }

        try {
            mergedTable = join(list1, list2);
        } catch (AssertionError e) {
            System.err.println("Error: input tables do not satisfy precondition.");
            System.exit(1);
        }

        for (Seq<String> row : mergedTable) {
            int count = 0;
            for (String singleD : row) {
                if (count >= 1) {
                    System.out.print(",");
                }
                System.out.print(singleD);
                count++;
            }
            System.out.println();
        }
    }
}