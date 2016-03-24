import javax.swing.text.Position;

/**
 * Created by Jly_wave on 10/25/15.
 */
public class Test_CNN {
    public static void main(String[] args) {
        double rate = 1;
        int convolution_Kernel_Scale = 2, subsampling_Kernel_Scale = 2;
        int t0_Number = 1, c1_Number = 2, s2_Number = 2, c3_Number = 3;
        int t0_Scale = 5, c1_Scale = 4, s2_Scale = 2, c3_Scale = 1;
        Convolution c1 = new Convolution(c1_Number, c1_Scale, convolution_Kernel_Scale);
        Convolution c3 = new Convolution(c3_Number, c3_Scale, convolution_Kernel_Scale);
        Subsampling s2 = new Subsampling(s2_Number, s2_Scale, subsampling_Kernel_Scale);
        int[] standard=new int[3];
        standard[0]=1;
        standard[1]=-1;
        standard[2]=0;
        for (int test = 0; test < 10; test++) {
            c1.initial();
            s2.initial();
            c3.initial();
            double[][] input = new double[][]{
                    {1, 0, 0, 1, 0},
                    {0, -1, 1, -1, 0},
                    {0, 0, -1, 0, 0},
                    {0, 0, -1, 1, 1},
                    {1, 0, 1, 1, -1}
            };
            for (int i = 0; i < c1_Number; i++) {
                c1.set_Layer(i, input);
                c1.set_Bias_Sigm(i);
            }
            //


            for (int i = 0; i < s2_Number; i++) {
                s2.set_Layer(i, c1.get_Layer(i));
            }


            for (int i = 0; i < c3_Number; i++) {
                for (int j = 0; j < s2_Number; j++) {
                    c3.set_Layer(i, s2.get_Layer(j));
                }
                c3.set_Bias_Sigm(i);
            }
            //test
//            System.out.println("c1 layer");
//            for (int i = 0; i < c1_Number; i++) {
//                for (int j = 0; j < c1_Scale; j++) {
//                    for (int k = 0; k < c1_Scale; k++) {
//                        System.out.print(c1.get_Layer(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//            System.out.println("s2 layer");
//            for (int i = 0; i < s2_Number; i++) {
//                for (int j = 0; j < s2_Scale; j++) {
//                    for (int k = 0; k < s2_Scale; k++) {
//                        System.out.print(s2.get_Layer(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//            System.out.println("c3 layer");
            for (int i = 0; i < c3_Number; i++) {
                for (int j = 0; j < c3_Scale; j++) {
                    for (int k = 0; k < c3_Scale; k++) {
                        System.out.print(c3.get_Layer(i)[j][k] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }



            //feedback
//            System.out.println("feedback");



            c3.set_Layer_Adjust(standard);
            for (int i = 0; i < c3_Number; i++) {
                for (int j = 0; j < s2_Number; j++) {
                    c3.set_Kernel_Adjust(i, s2.get_Layer(j));
                }
                c3.set_Bias_Adjust(i);
            }

            for (int i = 0; i < s2_Number; i++)
                for (int j = 0; j < c3_Number; j++) {
                    s2.set_Layer_Adjust(i, c3_Scale, convolution_Kernel_Scale, c3.get_Layer_Adjust(j), c3.get_Kernel(j),c3.get_Layer(j));
                }
            for (int i = 0; i < c1_Number; i++)
                c1.set_Layer_Adjust_Sub(i, subsampling_Kernel_Scale, s2.get_Max_Refer(i), s2.get_Layer_Adjust(i));
            for (int i = 0; i < c1_Number; i++) {
                c1.set_Kernel_Adjust(i, input);
                c1.set_Bias_Adjust(i);
            }










//            test
//            System.out.println("c3 kernel adjust");
//            for (int i = 0; i < c3_Number; i++) {
//                for (int j = 0; j < convolution_Kernel_Scale; j++) {
//                    for (int k = 0; k < convolution_Kernel_Scale; k++) {
//                        System.out.print(c3.get_Kernel_Adjust(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//            System.out.println("c3 bias adjust");
//            for (int i = 0; i < c3_Number; i++)
//                System.out.print(c3.get_Bias_Adjust(i) + " ");
//            System.out.println();
//            System.out.println("c1 bias adjust");
//            for (int i = 0; i < c1_Number; i++)
//                System.out.print(c1.get_Bias_Adjust(i) + " ");
//            System.out.println();
//            System.out.println("s2 layer adjust");
//            for (int i = 0; i < s2_Number; i++) {
//                for (int j = 0; j < s2_Scale; j++) {
//                    for (int k = 0; k < s2_Scale; k++) {
//                        System.out.print(s2.get_Layer_Adjust(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//            System.out.println("c1 layer adjust");
//            for (int i = 0; i < c1_Number; i++) {
//                for (int j = 0; j < c1_Scale; j++) {
//                    for (int k = 0; k < c1_Scale; k++) {
//                        System.out.print(c1.get_Layer_Adjust(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//            System.out.println("c1 kernel adjust");
//            for (int i = 0; i < c1_Number; i++) {
//                for (int j = 0; j < convolution_Kernel_Scale; j++) {
//                    for (int k = 0; k < convolution_Kernel_Scale; k++) {
//                        System.out.print(c1.get_Kernel_Adjust(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//            System.out.println("c3 kernel adjust");
//            for (int i = 0; i < c3_Number; i++) {
//                for (int j = 0; j < convolution_Kernel_Scale; j++) {
//                    for (int k = 0; k < convolution_Kernel_Scale; k++) {
//                        System.out.print(c3.get_Kernel_Adjust(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }



            c1.renew_Adjust(rate);
            c3.renew_Adjust(rate);
//            System.out.println("c3 kernel");
//            for (int i = 0; i < c1_Number; i++) {
//                for (int j = 0; j < convolution_Kernel_Scale; j++) {
//                    for (int k = 0; k < convolution_Kernel_Scale; k++) {
//                        System.out.print(c1.get_Kernel(i)[j][k] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
//            }
//
//            System.out.println("c1 bias");
//            for (int i = 0; i < c3_Number; i++)
//                System.out.print(c3.get_Bias(i) + " ");
//            System.out.println();
        }
    }
}
