import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static int t0_Number = 1, c1_Number = 6, s2_Number = 6, c3_Number = 16, s4_Number = 16, c5_Number = 120, f6_Number = 84, g7_Number = 10;
    static int t0_Scale = 32, c1_Scale = 28, s2_Scale = 14, c3_Scale = 10, s4_Scale = 5, c5_Scale = 1;
    static int convolution_Kernel_Scale = 5, subsampling_Kernel_Scale = 2;

    public static void main(String[] args) throws FileNotFoundException {
        //parameter
        double rate = 0.001;
        int section = 5, length = 3000, trainNumber = 300, fileNum = 3001;
        int leftBound = 1, rightBound = 301;
        String better_File="TestSave_926_300.txt";


        //input
        Data data=new Data(t0_Scale,c1_Number,c3_Number,c5_Number,f6_Number,g7_Number,convolution_Kernel_Scale);
        CNN cnn=new CNN(c1_Number,s2_Number,c3_Number,s4_Number,c5_Number,f6_Number,g7_Number,c1_Scale,c3_Scale,c5_Scale,convolution_Kernel_Scale,subsampling_Kernel_Scale);
        double[][] input = new double[t0_Scale][t0_Scale];
        double[][] test_Input=new double[t0_Scale][t0_Scale];
        int[] standard = new int[g7_Number];
        Convolution c1 = new Convolution(c1_Number, c1_Scale, convolution_Kernel_Scale);
        Convolution c3 = new Convolution(c3_Number, c3_Scale, convolution_Kernel_Scale);
        Convolution c5 = new Convolution(c5_Number, c5_Scale, convolution_Kernel_Scale);
        Subsampling s2 = new Subsampling(s2_Number, s2_Scale, subsampling_Kernel_Scale);
        Subsampling s4 = new Subsampling(s4_Number, s4_Scale, subsampling_Kernel_Scale);
        Bp f6 = new Bp(f6_Number, c5_Number);
        Bp g7 = new Bp(g7_Number, f6_Number);

        //test
        Convolution test_c1 = new Convolution(c1_Number, c1_Scale, convolution_Kernel_Scale);
        Convolution test_c3 = new Convolution(c3_Number, c3_Scale, convolution_Kernel_Scale);
        Convolution test_c5 = new Convolution(c5_Number, c5_Scale, convolution_Kernel_Scale);
        Subsampling test_s2 = new Subsampling(s2_Number, s2_Scale, subsampling_Kernel_Scale);
        Subsampling test_s4 = new Subsampling(s4_Number, s4_Scale, subsampling_Kernel_Scale);
        Bp test_f6 = new Bp(f6_Number, c5_Number);
        Bp test_g7 = new Bp(g7_Number, f6_Number);

        //better_Data
//        data.better(better_File,c1,c3,c5,f6,g7);

        //train
        Scanner inputFile,test_InputFile;
        int train,test, outputNum,test_OutputNum, trainNum = 0;
        int train_Dataset=0,train_NewTestSet=0;
        double best_Proportion=0;
        for (int trainStart = 0; trainStart < trainNumber; trainStart++) {
            for (train = 1; train < fileNum; train++) {
//                if (train >= leftBound && train < rightBound) continue;
                //initial
                cnn.initial(c1, s2, c3, s4, c5, f6, g7);

                //read input
                if (Math.random()>0.500) {
                    train_NewTestSet=(train_NewTestSet) % 3000+1;
                    if (train_NewTestSet==leftBound) train_NewTestSet=rightBound;
                    inputFile = new Scanner(new File("newTestSet/" + train_NewTestSet + ".txt"));
                } else{
                    train_Dataset=(train_Dataset) % 70000+1;
                    inputFile=new Scanner(new File("dataset/"+train_Dataset+".txt"));
                }
                for (int i=0;i<t0_Scale;i++)
                    for (int j=0;j<t0_Scale;j++){
                        input[i][j]=0;
                    }
                for (int i = 2; i < t0_Scale - 2; i++)
                    for (int j = 2; j < t0_Scale - 2; j++) {
                        input[i][j] = (double) inputFile.nextInt() / 255;
                    }
                outputNum = inputFile.nextInt();
                inputFile.close();
                for (int i = 0; i < g7_Number; i++) {
                    if (i == outputNum)
                        standard[i] = 1;
                    else
                        standard[i] = 0;
                }

                //forward
                cnn.forward(input, c1, s2, c3, s4, c5, f6, g7);
                //feedback
                cnn.feedback(standard, input, c1, s2, c3, s4, c5, f6, g7);
                //adjust
                if (train % section == 0) {
                    cnn.renew_Adjust(rate, c1, c3, c5, f6, g7);
                }

                //test
                trainNum++;
                if (trainNum % length == 0) {
                    int rightAns=0;
                    double max=0,now,sum_Proportion=0;
                    for (test=leftBound;test<rightBound;test++){
                        test_InputFile = new Scanner(new File("newTestSet/" + test + ".txt"));
                        for (int i=0;i<t0_Scale;i++)
                            for (int j=0;j<t0_Scale;j++){
                                test_Input[i][j]=0;
                            }
                        for (int i = 2; i < t0_Scale - 2; i++)
                            for (int j = 2; j < t0_Scale - 2; j++) {
                                test_Input[i][j] = (double) test_InputFile.nextInt() / 255;
                            }
                        test_OutputNum = test_InputFile.nextInt();
                        test_InputFile.close();
                        cnn.initial(test_c1, test_s2, test_c3, test_s4, test_c5, test_f6, test_g7);
                        for (int i=0;i<c1_Number;i++) {
                            test_c1.set_Kernel(i,c1.get_Kernel(i));
                            test_c1.set_Bias(i,c1.get_Bias(i));
                        }
                        for (int i=0;i<c3_Number;i++){
                            test_c3.set_Kernel(i,c3.get_Kernel(i));
                            test_c3.set_Bias(i, c3.get_Bias(i));
                        }
                        for (int i=0;i<c5_Number;i++){
                            test_c5.set_Kernel(i,c5.get_Kernel(i));
                            test_c5.set_Bias(i,c5.get_Bias(i));
                        }
                        for (int i=0;i<c5_Number;i++)
                            for (int j=0;j<f6_Number;j++) {
                                test_f6.set_Weight(i,j,f6.get_Weight(i,j));
                            }
                        for (int i=0;i<f6_Number;i++){
                            test_f6.set_Bias(i,f6.get_Bias(i));
                        }
                        for (int i=0;i<f6_Number;i++)
                            for (int j=0;j<g7_Number;j++) {
                                test_g7.set_Weight(i, j, g7.get_Weight(i, j));
                            }
                        for (int i=0;i<g7_Number;i++){
                            test_g7.set_Bias(i, g7.get_Bias(i));
                        }
                        cnn.forward(test_Input, test_c1, test_s2, test_c3, test_s4, test_c5, test_f6, test_g7);
                        max=0;
                        int maxNumber=0;
                        for (int i=0;i<g7_Number;i++){
                            now=test_g7.get_Layer(i);
                            if (now > max){
                                max=now;
                                maxNumber=i;
                            }
                        }
                        if (maxNumber==test_OutputNum){
                            rightAns++;
                        }
                    }
                    sum_Proportion=(rightAns+0.0)/(rightBound-leftBound);
                    System.out.println("Train "+trainNum+" Test "+(rightBound-leftBound)+" : The right proportion is "+sum_Proportion);
                    if (sum_Proportion>best_Proportion){
                        best_Proportion=sum_Proportion;
                        data.save(c1,c3,c5,f6,g7);
                    }
                }


            }
        }
    }


}
