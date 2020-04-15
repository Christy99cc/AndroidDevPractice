package bjfu.it.zhangsixuan.calculator;

import java.util.Stack;

import static java.lang.Math.sqrt;

public class MyCalculator {
    static private Stack<Character> operStack = new Stack<>();
    static private Stack<Double> numStack = new Stack<>();

    // 计算得到结果
    static String getResult(String expression) {
        String result = "";

        // 遍历String
        String temp = "";
        for (int i = 0; i < expression.length(); i++) {

            char c = expression.charAt(i);

            // 判断数字，小数
            if (c >= '0' && c <= '9' || c == '.') {
                temp += c;
                System.out.println("temp:" + temp);
            } else {
                // 字符串转数字
                if (!temp.equals("")) {
                    double num = Double.parseDouble(temp);

                    //数字进栈
                    numStack.push(num);

                    // 临时字符串清空
                    temp = "";
                }

                if (c == '=') {
                    while (!operStack.empty()) {
                        result = operateStackTop();
                        if (!result.equals("")) //错误
                            return result;
                    }
                    result = String.valueOf(numStack.pop());
                    return result;
                }

                // 直接入栈
                if (c == '(')
                    operStack.push(c);

                if (c == ')') {
                    while (!operStack.peek().equals('(')) {
                        operateStackTop();
                    }
                    operStack.pop(); //弹出'('
                }

                // 判断符号
                if (c == '+' || c == '-' || c == 'x' || c == '/' || c == '√') {
                    // 如果栈不空，要比较优先级
                    if (!operStack.empty()) {
                        char cTop = operStack.peek();
                        // 符号栈里的优先级高，弹出操作数，计算后压栈
                        // 优先级低的符号压栈
                        if (getPriorty(cTop) >= getPriorty(c)) {
                            result = operateStackTop();
                            if (!result.equals("")) {
                                return result; //出错了
                            }
                        }
                    }
                    operStack.push(c);
                }
            }
        }
        return result;
    }

    // 操作符号栈顶，分单操作数和双操作数
    static String operateStackTop() {
        char cTop = operStack.pop();
        // 单操作数√
        if (cTop == '√') {
            double n1 = numStack.pop();
            if (n1 < 0) {
                return "Cannot square negative numbers";
            }
            n1 = sqrt(n1);
            numStack.push(n1);
        } else {
            // 双操作数 + - x /

            double n2 = (!numStack.empty()) ? numStack.pop() : 0;
            double n1 = (!numStack.empty()) ? numStack.pop() : 0;

            // 除法的合法性判断
            if (cTop == '/') {
                if (n2 == 0) {
                    return "Cannot divide by Zero";
                }
            }
            double resTemp = getRes(n1, n2, cTop);
            numStack.push(resTemp);
        }
        // 正确的情况
        return "";
    }

    // 操作符的优先级
    private static int getPriorty(char cTop) {
        int level = 0;
        if (cTop == '+' || cTop == '-')
            level = 1;
        if (cTop == 'x' || cTop == '/')
            level = 2;
        if (cTop == '√')
            level = 3;
        return level;
    }

    // 四则运算，二元操作符
    static double getRes(double n1, double n2, char cTop) {
        double res = 0;
        switch (cTop) {
            case '+':
                res = n1 + n2;
                break;
            case '-':
                res = n1 - n2;
                break;
            case 'x':
                res = n1 * n2;
                break;
            case '/':
                res = n1 * 1.0 / n2;
                break;
            default:
                break;
        }
        return res;
    }
}
