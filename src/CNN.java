/**
 * Created by Jly_wave on 10/27/15.
 */
public class CNN {
    private int t0_Numbe, c1_Number, s2_Number, c3_Number, s4_Number , c5_Number, f6_Number , g7_Number ;
    private int convolution_Kernel_Scale, subsampling_Kernel_Scale,c1_Scale,c3_Scale,c5_Scale;
    private int[][] map = {
            {1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1},
            {1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1},
            {1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1},
            {0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1},
            {0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
    };
    public CNN(int c1_Number,int s2_Number,int c3_Number,int s4_Number,int c5_Number,int f6_Number,
               int g7_Number,int c1_Scale,int c3_Scale,int c5_Scale,int convolution_Kernel_Scale,int subsampling_Kernel_Scale){
        this.c1_Number=c1_Number;
        this.s2_Number=s2_Number;
        this.c3_Number=c3_Number;
        this.s4_Number=s4_Number;
        this.c5_Number=c5_Number;
        this.f6_Number=f6_Number;
        this.g7_Number=g7_Number;
        this.c1_Scale=c1_Scale;
        this.c3_Scale=c3_Scale;
        this.c5_Scale=c5_Scale;
        this.convolution_Kernel_Scale=convolution_Kernel_Scale;
        this.subsampling_Kernel_Scale=subsampling_Kernel_Scale;
    }


    public void renew_Adjust(double rate,Convolution c1, Convolution c3, Convolution c5, Bp f6, Bp g7){
        c1.renew_Adjust(rate);
        c3.renew_Adjust(rate);
        c5.renew_Adjust(rate);
        f6.renew_Adjust(rate);
        g7.renew_Adjust(rate);
    }
    public  void initial(Convolution c1, Subsampling s2, Convolution c3, Subsampling s4, Convolution c5, Bp f6, Bp g7){
        c1.initial();
        s2.initial();
        c3.initial();
        s4.initial();
        c5.initial();
        f6.initial();
        g7.initial();
    }
    public void forward(double[][] input, Convolution c1, Subsampling s2, Convolution c3, Subsampling s4, Convolution c5, Bp f6, Bp g7) {
        for (int i = 0; i < c1_Number; i++) {
            c1.set_Layer(i, input);
            c1.set_Bias_Sigm(i);
        }
        for (int i = 0; i < s2_Number; i++) {
            s2.set_Layer(i, c1.get_Layer(i));
        }
        for (int i = 0; i < c3_Number; i++) {
            for (int j = 0; j < s2_Number; j++)
                if (map[j][i] == 1) {
                    c3.set_Layer(i, s2.get_Layer(j));
                }
            c3.set_Bias_Sigm(i);
        }
        for (int i = 0; i < s4_Number; i++) {
            s4.set_Layer(i, c3.get_Layer(i));
        }
        for (int i = 0; i < c5_Number; i++) {
            for (int j = 0; j < s4_Number; j++) {
                c5.set_Layer(i, s4.get_Layer(j));
            }
            c5.set_Bias_Sigm(i);
        }
        for (int i = 0; i < f6_Number; i++) {
            for (int j = 0; j < c5_Number; j++) {
                f6.set_Layer(i, c5.get_Layer(j)[0][0], j);
            }
        }
        f6.addBiasRelu();
        for (int i = 0; i < g7_Number; i++) {
            for (int j = 0; j < f6_Number; j++) {
                g7.set_Layer(i, f6.get_Layer(j), j);
            }
        }
        g7.addBias();
        g7.softMax();
    }

    public  void feedback(int[] standard ,double[][] input,Convolution c1, Subsampling s2, Convolution c3, Subsampling s4, Convolution c5, Bp f6, Bp g7) {
        //g7
        g7.set_Layer_Adjust(standard);
        for (int i = 0; i < g7_Number; i++) {
            for (int j = 0; j < f6_Number; j++) {
                g7.set_Weight_Adjust(j, i, f6.get_Layer(j));
            }
            g7.set_Bias_Adjust(i);
        }
        //f6
        for (int i = 0; i < g7_Number; i++)
            for (int j = 0; j < f6_Number; j++) {
                f6.set_Layer_Adjust(j, g7.get_Layer_Adjust(i), g7.get_Layer(i), g7.get_Weight(j, i));
            }
        for (int i = 0; i < f6_Number; i++) {
            for (int j = 0; j < c5_Number; j++) {
                f6.set_Weight_Adjust(j, i, c5.get_Layer(j)[0][0]);
            }
            f6.set_Bias_Adjust(i);
        }
        //c5
        for (int i = 0; i < f6_Number; i++)
            for (int j = 0; j < c5_Number; j++) {
                c5.set_Layer_Adjust_Bp(j, f6.get_Layer_Adjust(i),f6.get_Layer(i),f6.get_Weight(j, i));
            }
        for (int i = 0; i < c5_Number; i++) {
            for (int j = 0; j < s4_Number; j++) {
                c5.set_Kernel_Adjust(i, s4.get_Layer(j));
            }
            c5.set_Bias_Adjust(i);
        }
        //s4
        for (int i = 0; i < s4_Number; i++)
            for (int j = 0; j < c5_Number; j++) {
                s4.set_Layer_Adjust(i, c5_Scale, convolution_Kernel_Scale, c5.get_Layer_Adjust(j), c5.get_Kernel(j), c5.get_Layer(j));
            }
        //c3
        for (int i = 0; i < c3_Number; i++)
            c3.set_Layer_Adjust_Sub(i, subsampling_Kernel_Scale, s4.get_Max_Refer(i), s4.get_Layer_Adjust(i));
        for (int i = 0; i < c3_Number; i++) {
            for (int j = 0; j < s2_Number; j++)
                if (map[j][i] == 1) {
                    c3.set_Kernel_Adjust(i, s2.get_Layer(j));
                }
            c3.set_Bias_Adjust(i);
        }
        //s2
        for (int i = 0; i < s2_Number; i++)
            for (int j = 0; j < c3_Number; j++)
                if (map[i][j] == 1) {
                    s2.set_Layer_Adjust(i, c3_Scale, convolution_Kernel_Scale, c3.get_Layer_Adjust(j), c3.get_Kernel(j),c3.get_Layer(j));
                }
        //c1
        for (int i = 0; i < c1_Number; i++)
            c1.set_Layer_Adjust_Sub(i, subsampling_Kernel_Scale, s2.get_Max_Refer(i), s2.get_Layer_Adjust(i));
        for (int i = 0; i < c1_Number; i++) {
            c1.set_Kernel_Adjust(i, input);
            c1.set_Bias_Adjust(i);
        }

    }
}
