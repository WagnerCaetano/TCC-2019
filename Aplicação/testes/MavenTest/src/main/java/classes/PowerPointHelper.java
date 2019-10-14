/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.apache.poi.hssf.usermodel.HSSFShapeTypes.Rectangle;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 *
 * @author u18300
 */
public class PowerPointHelper {
    private XMLSlideShow ppt;
    
    public PowerPointHelper()
    {
        ppt = new XMLSlideShow();
        ppt.createSlide();
    }
    public void setPowerPoint(String path) throws FileNotFoundException, IOException
    {
        ppt = new XMLSlideShow(new FileInputStream(path));
    }
    public void saveSlide(String path) throws IOException
    {
        FileOutputStream out = new FileOutputStream(path);
        ppt.write(out);
        out.close();
    }
    public void changeOrder(int oldPos,int newPos)
    {
        XSLFSlide[] slides = ppt.getSlides();
        XSLFSlide slide = slides[oldPos];
        ppt.setSlideOrder(slide, newPos);
    }
    public void deleteSlide(int Pos)
    {
        ppt.removeSlide(Pos);
    }
    public void addSlide()
    {
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];
        
        XSLFSlideLayout layout = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        XSLFSlide slide = ppt.createSlide(layout);
        
        XSLFTextShape titleShape = slide.getPlaceholder(0);
        XSLFTextShape contentShape = slide.getPlaceholder(1);
        
        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFAutoShape) {
            // this is a template placeholder
            }
        }
    }
    public void addImage(String path) throws FileNotFoundException, IOException
    {
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];
        
        XSLFSlideLayout layout = defaultMaster.getSlideLayouts()[0];
        XSLFSlide slide;
        slide = ppt.createSlide(layout);
        
        XSLFTextShape titleShape = slide.getPlaceholder(0);
        XSLFTextShape contentShape = slide.getPlaceholder(1);
        
        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFAutoShape) {
            // this is a template placeholder
            }
        }
        byte[] pictureData = IOUtils.toByteArray(new FileInputStream(path));
 
        int pd = ppt.addPicture(pictureData, XSLFPictureData.PICTURE_TYPE_PNG);
        XSLFPictureShape picture = slide.createPicture(pd);
        picture.setAnchor(new Rectangle(320, 230, 100, 92));
    }
    
    public void addImage(int pos,String path) throws FileNotFoundException, IOException
    {
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];
        
        XSLFSlideLayout layout = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        XSLFSlide slide = ppt.createSlide(layout);
        
        XSLFTextShape titleShape = slide.getPlaceholder(0);
        XSLFTextShape contentShape = slide.getPlaceholder(1);
        
        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFAutoShape) {
            // this is a template placeholder
            }
        }
        
        byte[] pictureData = IOUtils.toByteArray(new FileInputStream(path));
 
        int pd = ppt.addPicture(pictureData, XSLFPictureData.PICTURE_TYPE_PNG);
        XSLFPictureShape picture = slide.createPicture(pd);
        //picture.setAnchor(new Rectangle(320, 230, 100, 92));
    }
    public BufferedImage getImage(int pos)
    {
        Dimension pgsize = ppt.getPageSize();
        XSLFSlide[] slide = ppt.getSlides();
      
        BufferedImage img = null;
        img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();

        //clear the drawing area
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

        //render
        slide[pos].draw(graphics);
        return img;
    }
    
    public BufferedImage[] getSlides()
    {
      BufferedImage[] slides = new BufferedImage[ppt.getSlides().length];
      Dimension pgsize = ppt.getPageSize();
      XSLFSlide[] slide = ppt.getSlides();
      
      BufferedImage img = null;
      
      for (int i = 0; i < slide.length; i++) {
         img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.TYPE_INT_RGB);
         Graphics2D graphics = img.createGraphics();

         //clear the drawing area
         graphics.setPaint(Color.white);
         graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

         //render
         slide[i].draw(graphics);
         slides[i] = img;
      }
      return slides;
    }
    
}
