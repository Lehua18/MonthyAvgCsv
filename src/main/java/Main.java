import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        File csvFile = uploadCsvFile();
        assert csvFile != null;
        System.out.println(csvFile.getName());
        ArrayList<Double>[] arr = getValuesFromCsv(csvFile);
        List<String[]> newArr = new ArrayList<>();
        int month = 1;
        int count = 0; //counts place in arr
        int outerCount = 0; //counts number of loops
        while(count< arr.length){
            double total = 0;
            int innerCount= 0; //counts number of terms used
            while(count<arr.length && arr[count].get(1) == month){
                total+= arr[count].get(3);
                count++;
                innerCount++;
            }
            //Adds '0' to the beginning of each month if necessary
            String monthStr;
            if (month < 10)
                monthStr = "0" + month;
            else
                monthStr = month + "";

            //In order, adds year, adds month, sets day to 1 (for graphing program), and adds value
            newArr.add(new String[]{""+arr[count-1].get(0), monthStr, "01",""+ total/((double)innerCount)}); //Adds avg
            month = Integer.parseInt(String.valueOf(arr[count].get(1)));
            outerCount++;
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("monthlyAvgs.csv"))) {
            writer.writeAll(newArr);
            System.out.println("CSV file created successfully with OpenCSV");
        } catch (IOException e) {
            System.err.println("Error writing CSV file with OpenCSV: " + e.getMessage());
        }

    }

    public static File uploadCsvFile(){
        //create JFileChooser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open File");

        //Filters files shown to only csv files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv", "CSV");
        chooser.setFileFilter(filter);

        //Open chooser and get file
        int returnValue = chooser.showOpenDialog(new JFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println(file.getName());
            return file;
        }else{
            System.out.println("Something went wrong: Code "+returnValue);
            return null;
        }
    }

    public static ArrayList<Double>[] getValuesFromCsv(File file){
        try {
            ArrayList<Double>[] arr = new ArrayList[4];
            Scanner scan = new Scanner(file);
            ArrayList<Double> vals = new ArrayList<>();
            ArrayList<Double> year = new ArrayList<>();
            ArrayList<Double> month = new ArrayList<>();
            ArrayList<Double> day = new ArrayList<>();
            scan.nextLine(); //So titles aren't taken as data
            while (scan.hasNextLine()){
                String line = scan.nextLine();
                String[] fields = line.split(",");
                if(fields.length == 4) {
                    year.add(Double.parseDouble(fields[0]));
                    month.add(Double.parseDouble(fields[1]));
                    day.add(Double.parseDouble(fields[2]));
                    vals.add(Double.parseDouble(fields[3]));
                }
            }
            arr[0] = year;
            arr[1] = month;
            arr[2] = day;
            arr[3] = vals;
            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
