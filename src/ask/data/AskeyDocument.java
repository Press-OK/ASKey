package ask.data;

import java.io.Serializable;

public class AskeyDocument implements Serializable {
	
	private String filename = "";
	
	private int rasterWidth = 0;
	private int rasterHeight = 0;
	private int asciiWidth = 0;
	private int asciiHeight = 0;
	
	private String fontname = null;
	private int charH = 0;
	private int charW = 0;
	
	private int rasterColorBGR = 0;
	private int rasterColorBGG = 0;
	private int rasterColorBGB = 0;
	private int asciiColorBGR = 0;
	private int asciiColorBGG = 0;
	private int asciiColorBGB = 0;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getRasterWidth() {
		return rasterWidth;
	}
	public void setRasterWidth(int rasterWidth) {
		this.rasterWidth = rasterWidth;
	}
	public int getRasterHeight() {
		return rasterHeight;
	}
	public void setRasterHeight(int rasterHeight) {
		this.rasterHeight = rasterHeight;
	}
	public int getAsciiWidth() {
		return asciiWidth;
	}
	public void setAsciiWidth(int asciiWidth) {
		this.asciiWidth = asciiWidth;
	}
	public int getAsciiHeight() {
		return asciiHeight;
	}
	public void setAsciiHeight(int asciiHeight) {
		this.asciiHeight = asciiHeight;
	}
	public String getFontname() {
		return fontname;
	}
	public void setFontname(String fontname) {
		this.fontname = fontname;
	}
	public int getCharH() {
		return charH;
	}
	public void setCharH(int charH) {
		this.charH = charH;
	}
	public int getCharW() {
		return charW;
	}
	public void setCharW(int charW) {
		this.charW = charW;
	}
	public int getRasterColorBGR() {
		return rasterColorBGR;
	}
	public void setRasterColorBGR(int rasterColorBGR) {
		this.rasterColorBGR = rasterColorBGR;
	}
	public int getRasterColorBGG() {
		return rasterColorBGG;
	}
	public void setRasterColorBGG(int rasterColorBGG) {
		this.rasterColorBGG = rasterColorBGG;
	}
	public int getRasterColorBGB() {
		return rasterColorBGB;
	}
	public void setRasterColorBGB(int rasterColorBGB) {
		this.rasterColorBGB = rasterColorBGB;
	}
	public int getAsciiColorBGR() {
		return asciiColorBGR;
	}
	public void setAsciiColorBGR(int asciiColorBGR) {
		this.asciiColorBGR = asciiColorBGR;
	}
	public int getAsciiColorBGG() {
		return asciiColorBGG;
	}
	public void setAsciiColorBGG(int asciiColorBGG) {
		this.asciiColorBGG = asciiColorBGG;
	}
	public int getAsciiColorBGB() {
		return asciiColorBGB;
	}
	public void setAsciiColorBGB(int asciiColorBGB) {
		this.asciiColorBGB = asciiColorBGB;
	}
}