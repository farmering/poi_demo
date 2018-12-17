package util;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
/**
 * 数学公式计算
 * @author Administrator
 *
 */
public class FormulaCalculation {
	public static void main(String[] args) {
		FormulaCalculation formulaCalculation=new FormulaCalculation();
		ScriptEngineManager sem=new ScriptEngineManager();
		ScriptEngine engine=sem.getEngineByExtension("js");
		String formula="(X+Y)*2+X*10+Z";
		formula=formula.replaceAll("X", "10");
		formula=formula.replaceAll("Y", "5");
		formula=formula.replaceAll("Z", "5.233");
		try {
			Double Y=formulaCalculation.scaleNum(Double.parseDouble(engine.eval(formula).toString()));
			System.out.println(Y);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保留2位小数，四舍五入
	 */
	private Double scaleNum(Double num) {
		return new BigDecimal(num).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
