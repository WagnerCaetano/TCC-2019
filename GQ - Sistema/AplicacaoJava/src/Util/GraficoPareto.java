/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.SortOrder;

/**
 *
 * @author 00
 */
public class GraficoPareto extends ApplicationFrame{
    BDConexao c = new BDConexao();
    
    public GraficoPareto(final String title) throws SQLException, ClassNotFoundException {

        super(title);

        final DefaultKeyedValues data = new DefaultKeyedValues();
        Connection con = c.criaConexao();
        ResultSet rs = CRUDTodos.getCausas(con);
        while (rs.next())
            data.addValue(rs.getString("cauCausa"),rs.getInt("cauFrequencia"));
        con.close();
        /*data.addValue("Problema 1", new Integer(4843));
        data.addValue("Problema 2", new Integer(2098));
        data.addValue("Problema 3", new Integer(26));
        data.addValue("Problema 4", new Integer(1901));
        data.addValue("Problema 5", new Integer(2507));
        data.addValue("Problema 6", new Integer(1689));
        data.addValue("Problema 7", new Integer(948));
        data.addValue("Problema 8", new Integer(100));
        data.addValue("Problema 9", new Integer(263));
        data.addValue("Problema 10", new Integer(485));*/

        data.sortByValues(SortOrder.DESCENDING);
        final KeyedValues cumulative = DataUtilities.getCumulativePercentages(data);
        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Causas", data);

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "GRÁFICO DE PARETO",  // chart title
            "Causas de problemas",                     // domain axis label
            "Frequências",                     // range axis label
            dataset,                        // data
            PlotOrientation.VERTICAL,
            true,                           // include legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.addSubtitle(new TextTitle("Análise das causas"));

        // set the background color for the chart...
        chart.setBackgroundPaint(new Color(214,217,223));

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

        final CategoryDataset dataset2 = DatasetUtilities.createCategoryDataset(
            "Acumulativo", cumulative
        );
        final NumberAxis axis2 = new NumberAxis("Porcentagem");
        axis2.setNumberFormatOverride(NumberFormat.getPercentInstance());
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 500));
        setContentPane(chartPanel);
        
        
        /**
         * 
         * public static void main(final String[] args) {

            final ParetoChartDemo demo = new ParetoChartDemo("Pareto Chart Demo");
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);

    }
         */

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
}
