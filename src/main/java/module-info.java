/**
 * 
 */
/**
 * @author SBUSER
 *
 */
module elijah {
	
	exports com.example.app;

	exports tripleo.elijah;
	exports tripleo.elijah.gen.java;
	exports tripleo.elijah.lang;

	exports tripleo.elijah.util;

	exports antlr;
	exports antlr.actions.java;
	exports antlr.debug.misc;
	exports antlr.actions.cpp;
	exports antlr.actions.sather;
	exports antlr.preprocessor;
	exports antlr.collections;
	exports antlr.collections.impl;
	exports antlr.debug;

//	requires antlr;
	requires java.desktop;
	requires javassist;
	requires junit;
	requires org.eclipse.jdt.annotation;
//	requires tripleo;
}