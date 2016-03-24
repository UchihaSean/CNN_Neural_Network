import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
/**
 * Created by Jly_wave on 10/27/15.
 */
public class Final {
    static int t0_Number = 1, c1_Number = 6, s2_Number = 6, c3_Number = 16, s4_Number = 16, c5_Number = 120, f6_Number = 84, g7_Number = 10;
    static int t0_Scale = 32, c1_Scale = 28, s2_Scale = 14, c3_Scale = 10, s4_Scale = 5, c5_Scale = 1;
    static int convolution_Kernel_Scale = 5, subsampling_Kernel_Scale = 2;
    public static void main(String[]args) throws FileNotFoundException {
        CNN cnn=new CNN(c1_Number,s2_Number,c3_Number,s4_Number,c5_Number,f6_Number,g7_Number,c1_Scale,c3_Scale,c5_Scale,convolution_Kernel_Scale,subsampling_Kernel_Scale);
        Convolution c1 = new Convolution(c1_Number, c1_Scale, convolution_Kernel_Scale);
        Convolution c3 = new Convolution(c3_Number, c3_Scale, convolution_Kernel_Scale);
        Convolution c5 = new Convolution(c5_Number, c5_Scale, convolution_Kernel_Scale);
        Subsampling s2 = new Subsampling(s2_Number, s2_Scale, subsampling_Kernel_Scale);
        Subsampling s4 = new Subsampling(s4_Number, s4_Scale, subsampling_Kernel_Scale);
        Bp f6 = new Bp(f6_Number, c5_Number);
        Bp g7 = new Bp(g7_Number, f6_Number);

        Scanner inputFile=new Scanner(new File("TestSave_926_300.txt"));
        PrintWriter save = new PrintWriter("13302010038.txt");;
        double[][] kernel=new double[convolution_Kernel_Scale][convolution_Kernel_Scale];
        double[][] input = new double[t0_Scale][t0_Scale];
        double bias=0,weight=0;

        //c1
        for (int i=0;i<c1_Number;i++) {
            for (int j = 0; j < convolution_Kernel_Scale; j++)
                for (int k = 0; k < convolution_Kernel_Scale; k++)
                    kernel[j][k]=inputFile.nextDouble();
            c1.set_Kernel(i,kernel);
        }
        for (int i=0;i<c1_Number;i++){
            bias=inputFile.nextDouble();
            c1.set_Bias(i,bias);
        }
        //c3
        for (int i=0;i<c3_Number;i++) {
            for (int j = 0; j < convolution_Kernel_Scale; j++)
                for (int k = 0; k < convolution_Kernel_Scale; k++)
                    kernel[j][k]=inputFile.nextDouble();
            c3.set_Kernel(i,kernel);
        }
        for (int i=0;i<c3_Number;i++){
            bias=inputFile.nextDouble();
            c3.set_Bias(i,bias);
        }
        //c5
        for (int i=0;i<c5_Number;i++) {
            for (int j = 0; j < convolution_Kernel_Scale; j++)
                for (int k = 0; k < convolution_Kernel_Scale; k++)
                    kernel[j][k]=inputFile.nextDouble();
            c5.set_Kernel(i,kernel);
        }
        for (int i=0;i<c5_Number;i++){
            bias=inputFile.nextDouble();
            c5.set_Bias(i,bias);
        }
        //f6
        for (int i=0;i<c5_Number;i++)
            for (int j=0;j<f6_Number;j++){
                weight=inputFile.nextDouble();
                f6.set_Weight(i,j,weight);
            }
        for (int i=0;i<f6_Number;i++){
            bias=inputFile.nextDouble();
            f6.set_Bias(i,bias);
        }
        //g7
        for (int i=0;i<f6_Number;i++)
            for (int j=0;j<g7_Number;j++){
                weight=inputFile.nextDouble();
                g7.set_Weight(i, j, weight);
            }
        for (int i=0;i<g7_Number;i++){
            bias=inputFile.nextDouble();
            g7.set_Bias(i, bias);
        }


        //test
        Scanner inputFileName=new Scanner(System.in);
        String fileName=inputFileName.next();
        int fileNumber=inputFileName.nextInt();
        inputFileName.close();


        int rightAns=0;
        for (int test=1;test<fileNumber+1;test++){
            //initial
            cnn.initial(c1, s2, c3, s4, c5, f6, g7);

            //read input
            inputFile = new Scanner(new File(fileName+test+".txt"));
            for (int i=0;i<t0_Scale;i++)
                for (int j=0;j<t0_Scale;j++){
                    input[i][j]=0;
                }
            for (int i = 2; i < t0_Scale - 2; i++)
                for (int j = 2; j < t0_Scale - 2; j++) {
                    input[i][j] = (double) inputFile.nextInt() / 255;
                }
            inputFile.close();
            cnn.forward(input,c1,s2,c3,s4,c5,f6,g7);
            double max=0,now;
            int maxNumber=0;
            for (int i=0;i<g7_Number;i++){
                now=g7.get_Layer(i);
                if (now > max){
                    max=now;
                    maxNumber=i;
                }
            }
            save.println(maxNumber);
        }
        save.close();
    }
}
