// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IdentList.java

package tripleo.elijah.lang;

import java.util.*;

public class IdentList {

	List<String> idents=new ArrayList<String>();
	
	public void push(String aa){
		idents.add(aa);
	}
}

