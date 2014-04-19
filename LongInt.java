package com.example.LongInt;

import java.util.Vector;
import java.util.logging.*;

public class LongInt
{
    private Vector<Integer> num;		 //массив для цифр
    private int size;			 //размер массива
    private int sign;

    private static Logger log = Logger.getLogger(LongInt.class.getName());

    LongInt(int max) throws MyExeption
    {
        if(max < 0)
        {
            throw new MyExeption("max < 0 in LongInt\'s constructor");
        }
        num = new Vector<Integer>(max);
        for(int i = 0; i < max; ++i)
            num.add(0);
        size = max;
        log.fine("Create number");
    }

    LongInt(LongInt num2)
    {
        num = new Vector<Integer>(num2.size);
        size = num2.size;
        sign = num2.sign;
        for(int i = 0; i < num2.size; ++i)
        {
            num.add(num2.num.elementAt(i));
        }
    }

    LongInt(String StrT) throws MyExeption
    {
        Integer k;
        if(StrT.isEmpty())
            throw new MyExeption("Input string is empty.");
        if(StrT.charAt(0) != '-' && !Character.isDigit(StrT.charAt(0)))
            throw new MyExeption("Invalid first character of input.");
        if(StrT.charAt(0) == '-')
        {
            sign = 1;
            StrT = StrT.substring(1);
        }
    	size = StrT.length();
        num = new Vector<Integer>(size);
        for(int i = size-1; i >= 0; i--)
        {
            if(!Character.isDigit(StrT.charAt(i)))
                throw new MyExeption("Invalid input.");
            num.add(size-i-1, StrT.charAt(i) - 48);
        }
        DelForwardZero();
    }

    /////////////Сложение//////////////

    public LongInt Plus(LongInt num1) throws MyExeption
    {
        if(size == 0)
            return num1;
        if(num1.size == 0)
            return this;
        if(sign == 0 && num1.sign == 0)
            return Pl(num1);
        if(sign == 1 && num1.sign == 0)
        {
            if(this.ChangeSign().Comparison(num1) < 0)
            {
                return num1.Sub(this.ChangeSign());
            }
            else
            {
                return this.ChangeSign().Sub(num1).ChangeSign();
            }
        }
        if(sign == 0 && num1.sign == 1)
        {
            if(num1.ChangeSign().Comparison(this) < 0)
            {
                return Sub(num1.ChangeSign());
            }
            else
            {
                return num1.ChangeSign().Sub(this).ChangeSign();
            }
        }
        if(sign == 1 && num1.sign == 1)
        {
            return this.ChangeSign().Pl(num1.ChangeSign()).ChangeSign();
        }
        return new LongInt(0);
    }

    private LongInt Pl(LongInt num1) throws MyExeption
    {
        int max, min;
        if(size > num1.size)
        {
            max = size;
            min = num1.size;
        }
        else
        {
            max = num1.size;
            min = size;
        }

        LongInt num3 = new LongInt(max+1);

        int trans = 0;
        for(int i = 0; i <= max; ++i)
        {
            int ai = 0, bi = 0, ci = 0;
            if(i < size)
            {
                ai = num.elementAt(i);
            }
            else
            {
                ai = 0;
            }

            if(i < num1.size)
            {
                bi = num1.num.elementAt(i);
            }
            else
            {
                bi = 0;
            }

            ci = (ai + bi + trans) % 10;
            trans = (ai + bi + trans) / 10;
            num3.num.setElementAt(ci, i);
        }
        if(num3.num.elementAt(max) == 0)
        {
            num3.size = max;
        }
        return num3;
    }

    /////////Умножение//////////////

    private LongInt MultiOnDigit(int d) throws MyExeption
    {
        if(size == 0 || d == 0)
            return new LongInt(0);

        int max = size + 1, trans = 0;
        LongInt res = new LongInt(max);

        for(int i = 0; i < size; ++i)
        {
            int c = num.elementAt(i) * d + trans;
            res.num.setElementAt(c % 10, i);
            trans = c / 10;
        }
        if(trans != 0)
        {
            res.num.setElementAt(trans, size);
        }
        else
        {
            res.size--;
        }

        return res;
    }

    private LongInt MultiOnTenDeg(int i) throws MyExeption
    {
        if(size == 0)

            return new LongInt(0);
        LongInt res = new LongInt(size + i);
        for(int j = 0;j < size; ++j)
            res.num.setElementAt(num.elementAt(j), i + j);
        return res;
    }

    public LongInt Multi(LongInt num1) throws MyExeption
    {
        if(size == 0 || num1.size == 0)
            return new LongInt(0);

        int max = size + num1.size;
        LongInt num3 = new LongInt(max);
        if(sign == num1.size)
            num3.sign = 0;
        else
            num3.sign = 1;

        for(int i = 0; i < num1.size; ++i)
        {
            LongInt p_sum = MultiOnDigit(num1.num.elementAt(i)).MultiOnTenDeg(i);
            num3 = num3.Pl(p_sum);
        }

        while(num3.num.elementAt(num3.size - 1) == 0)
            num3.size--;
        return num3;
    }

    public LongInt Division(LongInt num1) throws MyExeption
    {
        if(num1.size == 0)
            //log.fine("division by zero");
            throw new MyExeption("Division by zero.");

        if(size == 0)
            return new LongInt(0);

        Integer d = 0;
        LongInt tmp1 = new LongInt(this.Abs());
        LongInt tmp2 = new LongInt(num1.Abs());

        while(tmp1.Comparison(tmp2) >= 0)
        {
            tmp1 = tmp1.Subtraction(tmp2);
            d++;
        }
        LongInt res = new LongInt(d.toString());
        if(res.size != 0)
        {
            if(sign == num1.size)
                res.sign = 0;
            else
                res.sign = 1;
        }
        return res;
    }

    public LongInt Abs()
    {
        LongInt num1 = new LongInt(this);
        num1.sign = 0;
        return num1;
    }

    //////////////Вычитание////////////////

    public int Comparison(LongInt num1)
    {
        if(sign == 0 && num1.sign == 0)
            return Comp(num1);
        if(sign == 1 && num1.sign == 0)
            return -1;
        if(sign == 0 && num1.sign == 1)
            return 1;
        if(sign == 1 && num1.sign == 1)
            return this.ChangeSign().Comp(num1.ChangeSign()) * (-1);
        return 9;
    }

    private int Comp(LongInt num1)
    {
        if(size > num1.size)
        {
            return 1;
        }
        else
        {
            if (size < num1.size)
            {
                return -1;
            }
            else
            {
                for(int i = size - 1;i >= 0;--i)
                {
                    if(num.elementAt(i) > num1.num.elementAt(i))
                    {
                        return 1;
                    }
                    else
                        if(num.elementAt(i)<num1.num.elementAt(i))
                        {
                            return -1;
                        }
                }
                return 0;
            }
        }
    }

   /* public LongInt Sub(LongInt num1)
    {
        LongInt num2 = new LongInt(this);
        int ci,ai,bi;
        LongInt num3 = new LongInt(size);
        for(int i = 0; i < size; ++i)
        {
            if(i < size)
            {
                ai = num2.num.elementAt(i);
            }
            else
            {
                ai = 0;
            }
            if(i < num1.size)
            {
                bi = num1.num.elementAt(i);
            }
            else
            {
                bi = 0;
            }
            if(ai - bi >= 0)
            {
                ci = ai - bi;
            }
            else
            {
                ci = 10 + ai - bi;
                //num2.num.setElementAt(num.elementAt(i+1)-1,i+1);
            }
            num2.num.setElementAt(ci,i);
        }
        return num2;
    }*/

    private LongInt Sub(LongInt num1)
    {
        LongInt num3 = new LongInt(this);
        int ai,bi,ci;
        for(int i = 0; i < num3.size; ++i)
        {
            if(i < num1.size)
            {
                bi = num1.num.elementAt(i);
            }
            else
            {
                bi = 0;
            }
           // bi = num1.num.elementAt(i);
            if(num3.num.elementAt(i) >= bi)
            {
                ai = num3.num.elementAt(i);

                //bi = num1.num.elementAt(i);
            }
            else
            {
                /*if(num3.num.elementAt(i + 1) != 0)
                {
                    ai = 10 + num3.num.elementAt(i);
                    num3.num.setElementAt(num.elementAt(i+1)-1,i+1);
                }
                else
                {*/
                    int j = i + 1;
                    while(num3.num.elementAt(j) == 0)
                    {
                        num3.num.setElementAt(9,j);
                        ++j;
                    }
                    ai = 10 + num3.num.elementAt(i);
                    num3.num.setElementAt(num3.num.elementAt(j)-1,j);
                //}
            }
            ci = ai - bi;
            num3.num.setElementAt(ci, i);
        }
        num3.DelForwardZero();
        return num3;
    }

   private void DelForwardZero()
   {
       for(int i = size - 1; i >=0 && num.elementAt(i) == 0; i--)
       {
           size--;
       }
   }

    private LongInt ChangeSign()
    {
        if(size == 0)
            return this;
        LongInt num1 = new LongInt(this);
        if(num1.sign == 0)
        {
            num1.sign = 1;
        }
        else
        {
            num1.sign = 0;
        }
        return num1;
    }

    public LongInt Subtraction(LongInt num1) throws MyExeption
    {
        /*int comp = Comp(num1);
        LongInt num3 = new LongInt(size);
        if(comp == 1)
        {
            num3 = Sub(num1);
        }
        else
        {
            if(comp == -1)
            {
                num3 = num1.Sub(this);
                num3 = num3.ChangeSign();
            }
        }
        num3.DelForwardZero();
        return num3;*/

        if(size == 0)
            return num1.ChangeSign();

        if(num1.size == 0)
            return this;

        if(this.Comparison(num1) == 0)
            return new LongInt(0);

        if(sign == 0 && num1.sign == 0)
        {
            if(Comparison(num1) < 0)
            {
                return num1.Sub(this).ChangeSign();
            }
            else
            {
                return Sub(num1);
            }
        }
        if(sign == 1 && num1.sign == 0)
        {
            return this.ChangeSign().Pl(num1).ChangeSign();
        }
        if(sign == 0 && num1.sign == 1)
        {
            return Pl(num1.ChangeSign());
        }
        if(sign == 1 && num1.sign == 1)
        {
            if(num1.ChangeSign().Comparison(this.ChangeSign()) < 0)
            {
                return this.ChangeSign().Sub(num1.ChangeSign()).ChangeSign();
            }
            else
            {
                return num1.ChangeSign().Sub(this.ChangeSign());
            }
        }
        return new LongInt(0);
    }

    public String toString()
    {
        String res = new String("");
        if(sign == 1)
        {
            res = "-";
        }
        if(size == 0)
        {
            return new String("0");
        }
        else
        {
            for(int i = size - 1; i >= 0; --i)
            {
                res = res.concat(Integer.toString(num.elementAt(i)));
            }
        }
        return res;
    }
}

class MyExeption extends Exception{
    MyExeption(String message)
    {
        super(message);
    }
}
