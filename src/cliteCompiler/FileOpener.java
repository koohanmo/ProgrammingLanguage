package cliteCompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOpener {

	private BufferedReader in;
	private StringBuilder TextBuilder;
	private String Text;
	private int pointer;
	
	FileOpener()
	{
		
		TextBuilder = new StringBuilder();
		pointer=0;
		try {
			
			in = new BufferedReader(new FileReader("source.txt"));
			String input;
			while((input=in.readLine())!=null)
			{
					
					TextBuilder.append(input+'\n');
				
			}
			Text=TextBuilder.toString();
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEnd()
	{
		if(Text.length()!=pointer) return false;
		else return true;
	
	}
	
	public char getChar()
	{
		if(isEnd()) return (char)-1;
		char result = Text.charAt(pointer);
		pointer++;
		return result;
	}
	
	
}
