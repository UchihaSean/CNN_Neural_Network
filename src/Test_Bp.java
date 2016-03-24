/**
 * Created by Jly_wave on 10/26/15.
 */
public class Test_Bp {
    public static void main(String[]args) {
        int rate=1;
        int c5_Number = 5, f6_Number = 3, g7_Number = 2, c5_Scale = 1;
        int convolution_Kernel_Scale = 2;
        Convolution c5 = new Convolution(c5_Number, c5_Scale, convolution_Kernel_Scale);
        Bp f6 = new Bp(f6_Number, c5_Number);
        Bp g7 = new Bp(g7_Number, f6_Number);
        for (int test=0;test<1000;test++) {
            c5.initial();
            f6.initial();
            g7.initial();
            c5.set_Layer();

            //
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
            g7.addBiasRelu();
            g7.softMax();
            //test
//            for (int i = 0; i < c5_Number; i++)
//                System.out.print(c5.get_Layer(i)[0][0] + " ");
//            System.out.println();
//            for (int i = 0; i < f6_Number; i++)
//                System.out.print(f6.get_Layer(i) + " ");
//            System.out.println();
            for (int i = 0; i < g7_Number; i++)
                System.out.print(g7.get_Layer(i) + " ");
            System.out.println();
//            //feedback
//            System.out.println();
//            System.out.println("feedback");
            int[] standard = new int[2];
            standard[0] = 1;
            standard[1] = 0;
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
                    c5.set_Layer_Adjust_Bp(j, f6.get_Layer_Adjust(i), f6.get_Layer(i), f6.get_Weight(j, i));
                }


            //test
//            for (int i = 0; i < g7_Number; i++)
//                System.out.print(g7.get_Layer_Adjust(i) + " ");
//            System.out.println();
//
//            for (int i = 0; i < g7_Number; i++) {
//                for (int j = 0; j < f6_Number; j++)
//                    System.out.print(g7.get_Weight_Adjust()[j][i] + " ");
//                System.out.println();
//            }
//            System.out.println("bias g7");
//            for (int i = 0; i < g7_Number; i++)
//                System.out.print(g7.get_Bias_Adjust(i) + " ");
//            System.out.println();
//
//            for (int i = 0; i < f6_Number; i++)
//                System.out.print(f6.get_Layer_Adjust(i) + " ");
//            System.out.println();
//
//            System.out.println("weights f6");
//            for (int i = 0; i < f6_Number; i++) {
//                for (int j = 0; j < c5_Number; j++)
//                    System.out.print(f6.get_Weight_Adjust()[j][i] + " ");
//                System.out.println();
//            }
//            System.out.println("bias f6");
//            for (int i = 0; i < f6_Number; i++)
//                System.out.print(f6.get_Bias_Adjust(i) + " ");
//            System.out.println();
//            System.out.println("layer c5");
//            for (int i = 0; i < c5_Number; i++)
//                System.out.print(c5.get_Layer_Adjust(i)[0][0] + " ");
//            System.out.println();
            c5.renew_Adjust(rate);
            f6.renew_Adjust(rate);
            g7.renew_Adjust(rate);

        }

    }
}
