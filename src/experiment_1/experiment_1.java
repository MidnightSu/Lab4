package experiment_1;

import java.util.ArrayList;
import java.util.Scanner;
/**
* 
*
* @author Tuomingxiang
*/
public class experiment_1 {
	//letter numbser新表达式需要初始化
	/**
	 * .
	 * @param args .
	 */
	public static void  main (String[] args){
		@SuppressWarnings("resource")
		Scanner in=new Scanner(System.in);
		String expression=null;//输入的表达式或者是！命令行
		String[] xiang;//这里存放的是将表达式分解为项的String数组
		int choice=0;//选择
		ArrayList<String>  number = new ArrayList<String> ();//记录赋值函数的值
		ArrayList<String>  letter = new ArrayList<String> ();//赋值函数的类型
		ArrayList<String>  fuhao = new ArrayList<String> ();//存放表达式中的+号或者是-号的位置顺序
		char varDerivative = 0;//保证求导变量的类型
		int j=0;
	//	String tmpExpression ="";
		
		Expressions clsExpression = new Expressions();//创建表达式对象
		
		while(true){
			choice=0;
			expression=in.nextLine();//输入
			fuhao=myfuhao(expression);//获得输入的符号，不管是不是正确的表达式或者是赋值求导指令
			

			if (expression.length()>=9 && expression.substring(0, 9).equals("!simplify")){//如果是赋值选择2
				choice=2;
			}
			else if(expression.length()>=4 && expression.substring(0, 4).equals("!d/d")){//如果是求导选择3
				choice=3;
			}
			else if(expression.length()<0 || expression.length()>0){
				choice=1;//其他情况选择1
			}
			else{
				continue;
			}
			switch(choice)
			{
			case 1:
					expression=change(expression,fuhao);//将表达式转变为标准的表达式（无论表达式是否正确或者是输入的赋值求导指令
					clsExpression.setExpressions(expression);//设置表达式
				break;
			case 2:
				String tmpExpression ="";
				tmpExpression = clsExpression.getExpression();
				if (tmpExpression.equals("")){
					System.out.println("Error There is no expression!");//在没有表达式的情况下无法赋值
				}
				else{
					for (int i=expression.length()-1;i>=0;i--){//获得要赋值的变量
						if(expression.charAt(i)=='='){
							j=i;
							number.add(expression.substring(i+1,j+2));//获得要赋值变量的值
						}
						else if(expression.charAt(i)==' '){
							letter.add(expression.substring(i+1, j));//获得要赋值变量的名称
						}
					}
					System.out.println(clsExpression.simplify(letter,number));//计算赋值后的结果，在函数中输出
				}
				number.clear();
				letter.clear();
				break;
			case 3:
			//	String tmpExpression ="";
				tmpExpression = clsExpression.getExpression();
				if(expression.equals("")){
					System.out.println("Error There is no expression!");//在没有表达式的情况下无法求导
				}
				else{//有了表达式
					//首先要将求导的变量求出来 变量只能是在 !d/d()括号里面的值 "()"是不能存在的 且变量只能有字母组成
					varDerivative=expression.charAt(4);//获得要求导的变量 这里变量只能是单个字符
					j=0;
					xiang=tmpExpression.split("\\+|\\-");
					fuhao=myfuhao(tmpExpression);
					System.out.println(clsExpression.derivative(xiang,varDerivative,tmpExpression,fuhao));//输出求导后的结果，在函数中输出
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 获取表达式的多项式的符号
	 * @param endexpression 输入表达式
	 * @return 多项式符号
	 */
	public static ArrayList<String> myfuhao(final String endexpression){
		ArrayList<String> fuhao=new ArrayList<String>();
		for (int i=0;i<endexpression.length();i++){
			if(endexpression.charAt(i)=='+'){
				fuhao.add("+");
			}
			else if(endexpression.charAt(i)=='-'){
				fuhao.add("-");
			}
		}
		return fuhao;
	}
	/**
	 * 去掉表达式中的tab和空格，将幂乘转换为连乘（尚未完成）
	 * @param expression 输入表达式
	 * @param fuhao 表达式符号
	 * @return 整理后的表达式
	 */
	public static String change(String expression,ArrayList<String> fuhao){//将输入的表达式进行修改
		String changeexpression=null;
		String endexpression="";
		String[] str; 
		String[] xiang;
		String newstr = null;
		String newxiang = null;
		int xishu=0;
		int fuhao1=0;//标记到第几个符号了
		changeexpression=expression.replace("\t", "");//将tab删掉
		changeexpression=changeexpression.replace(" ", "");//将空格删掉

		for (int i=0;i<changeexpression.length();i++){
			if(i<changeexpression.length()-1){
				if(changeexpression.charAt(i)>='0'&&changeexpression.charAt(i)<='9'){
					if(changeexpression.charAt(i+1)>='a'&&changeexpression.charAt(i+1)<='z'||changeexpression.charAt(i+1)>='A'&&changeexpression.charAt(i+1)<='Z'){
						changeexpression=changeexpression.substring(0,i+1)+"*"+changeexpression.substring(i+1);
					}
				}
			}
		}
		endexpression=changeexpression;
		str=endexpression.split("\\+|\\-");
		for (int i=0;i<str.length;i++){//依次访问每个项
			newstr=str[i];
			xiang=str[i].split("\\*");
			for(int j=0;j<xiang.length;j++){//对每个项访问
			//	newxiang=xiang[j];
				for(int k=0;k<xiang[j].length();k++){
					if(xiang[j].charAt(k)=='^'){
						for (int l=k+1;l<xiang[j].length();l++){//计算^后面的系数是多少
							if(xiang[j].charAt(l)<'0'||xiang[j].charAt(l)>'9'){//出现错误，直接返回，在judge中会判断为错误表达式
								return endexpression;
							}
							else{
								xishu=xishu*10+(xiang[j].charAt(l)-48);
							}
						}
						newxiang=newxiang.substring(0, newxiang.indexOf("^"));
						for (int m=0;m<xishu-1;m++){
							newxiang+="*"+xiang[j].substring(0, xiang[j].indexOf("^"));
						}
						xiang[j]=newxiang;
					}
				}
			}
			xishu=0;
			newxiang=xiang[0];
			for (int n=1;n<xiang.length;n++){
				newxiang+="*"+xiang[n];
			}
			str[i]=newxiang;
		}
		//每个项都处理完了，该合并项了
		endexpression="";
		for (int i=0;i<str.length;i++){
			endexpression+=str[i];
			if(i<str.length-1){
				endexpression+=fuhao.get(fuhao1++);
			}
		}
		return endexpression;
	}
}


