import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Jly_wave on 10/27/15.
 */
public class Data {
    private int c1_Number,c3_Number, c5_Number, f6_Number, g7_Number;
    private int convolution_Kernel_Scale,t0_Scale;
    public Data(int t0_Scale,int c1_Number,int c3_Number,int c5_Number,int f6_Number,
                int g7_Number,int convolution_Kernel_Scale){
        this.c1_Number=c1_Number;
        this.c3_Number=c3_Number;
        this.c5_Number=c5_Number;
        this.f6_Number=f6_Number;
        this.g7_Number=g7_Number;
        this.t0_Scale=t0_Scale;
        this.convolution_Kernel_Scale=convolution_Kernel_Scale;
    }
    public void save( Convolution c1, Convolution c3, Convolution c5, Bp f6, Bp g7) throws FileNotFoundException {
        PrintWriter save=new PrintWriter("TestSave.txt");
        //c1
        for (int i=0;i<c1_Number;i++){
            for (int j=0;j<convolution_Kernel_Scale;j++) {
                for (int k = 0; k < convolution_Kernel_Scale; k++) {
                    save.print(c1.get_Kernel(i)[j][k] + " ");
                }
                save.println();
            }
            save.println();
        }
        save.println();
        for (int i=0;i<c1_Number;i++)
            save.print(c1.get_Bias(i)+" ");
        save.println();

        //c3
        for (int i=0;i<c3_Number;i++){
            for (int j=0;j<convolution_Kernel_Scale;j++) {
                for (int k = 0; k < convolution_Kernel_Scale; k++) {
                    save.print(c3.get_Kernel(i)[j][k] + " ");
                }
                save.println();
            }
            save.println();
        }
        save.println();
        for (int i=0;i<c3_Number;i++)
            save.print(c3.get_Bias(i)+" ");
        save.println();

        //c5
        for (int i=0;i<c5_Number;i++){
            for (int j=0;j<convolution_Kernel_Scale;j++) {
                for (int k = 0; k < convolution_Kernel_Scale; k++) {
                    save.print(c5.get_Kernel(i)[j][k] + " ");
                }
                save.println();
            }
            save.println();
        }
        save.println();
        for (int i=0;i<c5_Number;i++)
            save.print(c5.get_Bias(i)+" ");
        save.println();

        //f6
        for (int i=0;i<c5_Number;i++){
            for (int j=0;j<f6_Number;j++)
                save.print(f6.get_Weight(i,j)+" ");
            save.println();
        }
        save.println();
        for (int i=0;i<f6_Number;i++)
            save.print(f6.get_Bias(i)+" ");
        save.println();

        //g7
        for (int i=0;i<f6_Number;i++){
            for (int j=0;j<g7_Number;j++)
                save.print(g7.get_Weight(i,j)+" ");
            save.println();
        }
        save.println();
        for (int i=0;i<g7_Number;i++)
            save.print(g7.get_Bias(i)+" ");
        save.println();
        save.close();
    }
    public void better(String file_Name,Convolution c1, Convolution c3, Convolution c5, Bp f6, Bp g7) throws FileNotFoundException {
        Scanner inputFile=new Scanner(new File(file_Name));
        double[][] kernel=new double[convolution_Kernel_Scale][convolution_Kernel_Scale];
        double[][] input = new double[t0_Scale][t0_Scale];
        double bias=0,weight=0;
        int outputNum=0;

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

    }
}
