package main

import org.junit.Assert._
import org.junit.Test
import simplifier.MPLDelorme._
//import scala.io.StdIn.readLine
//import simplifier.MPLDelorme._
import library._

class TestAccept {
  
  def ok(a:String,b:String):Boolean={
    val simp= new Simplify()
    val initial = Parser.parseSymbolList(a)
    val attendu = Parser.parseSymbolList(b)
    val resutat = simp.process(initial)
    resutat == attendu
  }
  
  @Test
  def t0(){
    val simp= new Simplify()
    val initial = Parser.parseSymbolList("aaaaa")
    val attendu = Parser.parseSymbolList("aaaaa")
    val resutat = simp.process(initial)
    assertTrue(resutat == attendu)
    assertTrue(ok("abcde","abcde"))
    assertTrue(!ok("abcde","abce"))
  }
  
  @Test
  def t1(){
    assertTrue(ok("abcde","abcde"))
  }
  
  @Test
  def t2(){
    assertTrue(ok("",""))
  }
  
  @Test
  def t3(){
    assertTrue(ok("***","*"))
  }
  
  @Test
  def t4(){
    assertTrue(ok("?","?"))
    assertTrue(ok("??","??"))
    assertTrue(ok("???","???"))
    assertTrue(ok("*?","*?"))
    assertTrue(ok("?*","?*"))
    assertTrue(ok("?a","?a"))
    assertTrue(ok("a?","a?"))
    assertTrue(ok("?a?","?a?"))
    assertTrue(ok("?*?","?*?"))
    assertTrue(ok("??*","??*"))
    assertTrue(ok("*??","*??"))
    assertTrue(ok("??*??","??*??"))
  }
  
  @Test
  def t5(){
    assertTrue(ok("a?*.txt","a?*.txt"))
  }
  
  @Test
  def t6(){
    assertTrue(ok("abcde****e?z??*??","abcde*e?z??*??"))
  }
  
  @Test
  def t7(){
    assertTrue(ok("********???????????????**********","*???????????????"))//*??????????????? ou ???????????????*
  }
  
  @Test
  def t8(){
    assertTrue(ok("*??*??*","*????"))
  }
  
  @Test
  def t9(){
    assertTrue(ok("*??*??*a*??*??*","*????a*????"))
  }
  
  @Test
  def t10(){
    assertTrue(ok("?*??*??*a*??*??*?","?*????a*?????"))
  }
}