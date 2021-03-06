theory tp67
imports Main  "~~/src/HOL/Library/Code_Target_Int" 
begin

(* Types des expressions, conditions et programmes (statement) *)
datatype expression= Constant int | Variable string | Sum expression expression | Sub expression expression

datatype condition= Eq expression expression

datatype statement= Seq statement statement | 
                    Aff string expression | 
                    Read string | 
                    Print expression | 
                    Exec expression | 
                    If condition statement statement |
                    Skip
(* Un exemple d'expression *)

(* expr1= (x-10) *)
definition "expr1= (Sub (Variable ''x'') (Constant 10))"


(* Des exemples de programmes *)

(* p1= exec(0) *)
definition "p1= Exec (Constant 0)"

(* p2= {
        print(10)
        exec(0+0)
       }
*)

definition "p2= (Seq (Print (Constant 10)) (Exec (Sum (Constant 0) (Constant 0))))"

(* p3= {
         x:=0
         exec(x)
       }
*)

definition "p3= (Seq (Aff ''x'' (Constant 0)) (Exec (Variable ''x'')))"

(* p4= {
         read(x)
         print(x+1)
       }
*)
definition "p4= (Seq (Read ''x'') (Print (Sum (Variable ''x'') (Constant 1))))"


(* Le type des evenements soit X: execute, soit P: print *)
datatype event= X int | P int

(* les flux de sortie, d'entree et les tables de symboles *)

type_synonym outchan= "event list"
definition "el1= [X 1, P 10, X 0, P 20]"                   (* Un exemple de flux de sortie *)

type_synonym inchan= "int list"           
definition "il1= [1,-2,10]"                                (* Un exemple de flux d'entree [1,-2,10]              *)

type_synonym symTable= "(string * int) list"
definition "(st1::symTable)= [(''x'',10),(''y'',12)]"      (* Un exemple de table de symbole *)


(* La fonction (partielle) de recherche dans une liste de couple, par exemple une table de symbole *)
datatype 'a option= None | Some 'a

fun assoc:: "'a \<Rightarrow> ('a * 'b) list \<Rightarrow> 'b option"
where
"assoc _ [] = None" |
"assoc x1 ((x,y)#xs)= (if x=x1 then Some(y) else (assoc x1 xs))"

(* Exemples de recherche dans une table de symboles *)

value "assoc ''x'' st1"     (* quand la variable est dans la table st1 *)
value "assoc ''z'' st1"     (* quand la variable n'est pas dans la table st1 *)


(* Evaluation des expressions par rapport a une table de symboles *)
fun evalE:: "expression \<Rightarrow> symTable \<Rightarrow> int"
where
"evalE (Constant s) e = s" |
"evalE (Variable s) e= (case (assoc s e) of None \<Rightarrow> -1 | Some(y) \<Rightarrow> y)" |
"evalE (Sum e1 e2) e= ((evalE e1 e) + (evalE e2 e))" |
"evalE (Sub e1 e2) e= ((evalE e1 e) - (evalE e2 e))" 

(* Exemple d'évaluation d'expression *)

value "evalE expr1 st1"

(* Evaluation des conditions par rapport a une table de symboles *)
fun evalC:: "condition \<Rightarrow> symTable \<Rightarrow> bool"
where
"evalC (Eq e1 e2) t= ((evalE e1 t) = (evalE e2 t))"

(* Evaluation d'un programme par rapport a une table des symboles, a un flux d'entree et un flux de sortie. 
   Rend un triplet: nouvelle table des symboles, nouveaux flux d'entree et sortie *)
fun evalS:: "statement \<Rightarrow> (symTable * inchan * outchan) \<Rightarrow> (symTable * inchan * outchan)"
where
"evalS Skip x=x" |
"evalS (Aff s e)  (t,inch,outch)=  (((s,(evalE e t))#t),inch,outch)" |
"evalS (If c s1 s2)  (t,inch,outch)=  (if (evalC c t) then (evalS s1 (t,inch,outch)) else (evalS s2 (t,inch,outch)))" |
"evalS (Seq s1 s2) (t,inch,outch)= 
    (let (t2,inch2,outch2)= (evalS s1 (t,inch,outch)) in
        evalS s2 (t2,inch2,outch2))" |
"evalS (Read _) (t,[],outch)= (t,[],outch)" |
"evalS (Read s) (t,(x#xs),outch)= (((s,x)#t),xs,outch)" |
"evalS (Print e) (t,inch,outch)= (t,inch,((P (evalE e t))#outch))" |
"evalS (Exec e) (t,inch,outch)= 
  (let res= evalE e t in
   (t,inch,((X res)#outch)))"



(* Exemples d'évaluation de programmes *)
(* Les programmes p1, p2, p3, p4 ont été définis plus haut *)
(* p1= exec(0) *)


value "evalS p1 ([],[],[])"

(* ------------------------------------ *)
(* p2= {
        print(10)
        exec(0+0)
       }
*)

value "evalS p2 ([],[],[])"

(* ------------------------------------ *)
(* p3= {
         x:=0
         exec(x)
       }
*)

value "evalS p3 ([],[],[])"

(* ------------------------------------ *)
(* p4= {
         read(x)
         print(x+1)
       }
*)

value "evalS p4 ([],[10],[])"


definition "bad1= (Exec (Constant 0))"
definition "bad2= (Exec (Sub (Constant 2) (Constant 2)))"
definition "bad3= (Seq (Aff ''x'' (Constant 1)) (Seq (Print (Variable ''x'')) (Exec (Sub (Variable ''x'') (Constant 1)))))"
definition "bad4= (Seq (Read ''x'') (Seq (If (Eq (Variable ''x'') (Constant 0)) Skip (Aff ''y'' (Constant 1))) (Exec (Sum (Variable ''y'') (Constant 1)))))"
definition "bad5= (Seq (Read ''x'') (Seq (Aff ''y'' (Sum (Variable ''x'') (Constant 2))) (Seq (If (Eq (Variable ''x'') (Sub (Constant 0) (Constant 1))) (Seq (Aff ''x'' (Sum (Variable ''x'') (Constant 2))) (Aff ''y'' (Sub (Variable ''y'') (Variable ''x'')))) (Seq (Aff ''x'' (Sub (Variable ''x'') (Constant 2))) (Aff ''y'' (Sub (Variable ''y'') (Variable ''x''))))) (Exec (Variable ''y'')))))"
definition "bad6= (Seq (Read ''x'') (Seq (If (Eq (Variable ''x'') (Constant 0)) (Aff ''z'' (Constant 1)) (Aff ''z'' (Constant 0))) (Exec (Variable ''z''))))"
definition "bad7= (Seq (Read ''x'') (Seq (If (Eq (Variable ''x'') (Constant 0)) (Aff ''z'' (Constant 0)) (Aff ''z'' (Constant 1))) (Exec (Variable ''z''))))"
definition "bad8= (Seq (Read ''x'') (Seq (Read ''y'') (If (Eq (Variable ''x'') (Variable ''y'')) (Exec (Constant 1)) (Exec (Constant 0)))))"
definition "ok0= (Seq (Aff ''x'' (Constant 1)) (Seq (Read ''y'') (Seq (If (Eq (Variable ''y'') (Constant 0)) (Seq (Print (Sum (Variable ''y'') (Variable ''x'')))
(Print (Variable ''x''))
) (Print (Variable ''y''))
) (Seq (Aff ''x'' (Constant 1)) (Seq (Print (Variable ''x''))
 (Seq (Aff ''x'' (Constant 2)) (Seq (Print (Variable ''x''))
 (Seq (Aff ''x'' (Constant 3)) (Seq (Print (Variable ''x''))
 (Seq (Read ''y'') (Seq (If (Eq (Variable ''y'') (Constant 0)) (Aff ''z'' (Sum (Variable ''x'') (Variable ''x''))) (Aff ''z'' (Sub (Variable ''x'') (Variable ''y'')))) (Print (Variable ''z''))
)))))))))))"
definition "ok1= (Seq (Aff ''x'' (Constant 1)) (Seq (Print (Sum (Variable ''x'') (Variable ''x'')))
 (Seq (Exec (Constant 10)) (Seq (Read ''y'') (If (Eq (Variable ''y'') (Constant 0)) (Exec (Constant 1)) (Exec (Constant 2)))))))"
definition "ok2= (Exec (Variable ''y''))"
definition "ok3= (Seq (Read ''x'') (Exec (Sum (Variable ''y'') (Constant 2))))"
definition "ok4= (Seq (Aff ''x'' (Constant 0)) (Seq (Aff ''x'' (Sum (Variable ''x'') (Constant 20))) (Seq (If (Eq (Variable ''x'') (Constant 0)) (Aff ''z'' (Constant 0)) (Aff ''z'' (Constant 4))) (Seq (Exec (Variable ''z'')) (Exec (Variable ''x''))))))"
definition "ok5= (Seq (Read ''x'') (Seq (Aff ''x'' (Constant 4)) (Exec (Variable ''x''))))"
definition "ok6= (Seq (If (Eq (Constant 1) (Constant 2)) (Aff ''x'' (Constant 0)) (Aff ''x'' (Constant 1))) (Exec (Variable ''x'')))"
definition "ok7= (Seq (Read ''x'') (Seq (If (Eq (Variable ''x'') (Constant 0)) (Aff ''x'' (Constant 1)) (If (Eq (Variable ''x'') (Constant 4)) (Aff ''x'' (Constant 1)) (Aff ''x'' (Constant 1)))) (Exec (Variable ''x''))))"
definition "ok8= (Seq (Read ''x'') (Seq (If (Eq (Variable ''x'') (Constant 0)) (Aff ''x'' (Constant 1)) (Aff ''x'' (Constant 2))) (Exec (Sub (Variable ''x'') (Constant 3)))))"
definition "ok9= (Seq (Read ''x'') (Seq (Read ''y'') (If (Eq (Sum (Variable ''x'') (Variable ''y'')) (Constant 0)) (Exec (Constant 1)) (Exec (Sum (Variable ''x'') (Sum (Variable ''y'') (Sum (Variable ''y'') (Variable ''x''))))))))"
definition "ok10= (Seq (Read ''x'') (If (Eq (Variable ''x'') (Constant 0)) (Exec (Constant 1)) (Exec (Variable ''x''))))"
definition "ok11= (Seq (Read ''x'') (Seq (If (Eq (Variable ''x'') (Constant 0)) (Aff ''x'' (Sum (Variable ''x'') (Constant 1))) Skip) (Exec (Variable ''x''))))"
definition "ok12= (Seq (Aff ''x'' (Constant 1)) (Seq (Read ''z'') (If (Eq (Variable ''z'') (Constant 0)) (Exec (Variable ''y'')) (Exec (Variable ''z'')))))"
definition "ok13= (Seq (Aff ''z'' (Constant 4)) (Seq (Aff ''x'' (Constant 1)) (Seq (Read ''y'') (Seq (Aff ''x'' (Sum (Variable ''x'') (Sum (Variable ''z'') (Variable ''x'')))) (Seq (Aff ''z'' (Sum (Variable ''z'') (Variable ''x''))) (Seq (If (Eq (Variable ''y'') (Constant 1)) (Aff ''x'' (Sub (Variable ''x'') (Variable ''y''))) Skip) (Seq (If (Eq (Variable ''y'') (Constant 0)) (Seq (Aff ''y'' (Sum (Variable ''y'') (Constant 1))) (Exec (Variable ''x''))) Skip) (Exec (Variable ''y'')))))))))"
definition "ok14= (Seq (Read ''x'') (Seq (Read ''y'') (If (Eq (Sum (Variable ''x'') (Variable ''y'')) (Constant 0)) (Exec (Constant 1)) (Exec (Sum (Variable ''x'') (Variable ''y''))))))"


(* Le TP commence ici! *)

(*  Réponse 3.1 

  Tous les pX sont aggressif car ils peuvent faire des exec 0 dans au moins un cas.

*)

(* 3.4 *)

fun BAD::"(symTable * inchan * outchan) \<Rightarrow> bool"
  where
"BAD (_, _, []) = False" |
"BAD (s, i, (x#l)) = (if (x=(X 0)) then True else (BAD (s, i, l)))"

(* 4.1 *)


fun sanv1::"statement \<Rightarrow> bool"
  where
"sanv1 (Exec e) = False" |
"sanv1 (If _ s1 s2) = (sanv1 s1 \<and> sanv1 s2)" |
"sanv1 (Seq s1 s2) = (sanv1 s1 \<and> sanv1 s2)" |
"sanv1 _ = True" 

value"sanv1 p4"

lemma sanv1:"sanv1 x \<longrightarrow> ( \<forall>i.\<not>BAD( evalS x ([],i,[])))"
  nitpick
  oops

(* 4.2 *)

fun sanv2::"statement \<Rightarrow> bool"
  where
"sanv2 (Exec (Constant x)) = (x \<noteq> 0)" |
"sanv2 (Exec _ ) = False" |
"sanv2 (If _ s1 s2) = (sanv2 s1 \<and> sanv2 s2)" |
"sanv2 (Seq s1 s2) = (sanv2 s1 \<and> sanv2 s2)" |
"sanv2 _ = True"

value"sanv2 p4"

lemma sanv2:"sanv2 x \<longrightarrow> ( \<forall>i.\<not>BAD( evalS x ([],i,[])))"
  nitpick
  oops

(* 4.3 *)

datatype Anyval = Myint int | Any
datatype AnyCond = Mybool bool | AnyCond
type_synonym symTable2= "(string * Anyval) list"

(* Fonction de recherche pour la table de symbole *)

fun assoc2:: "string \<Rightarrow> symTable2 \<Rightarrow> Anyval"
  where
"assoc2 s [] = Myint (-1)" |
"assoc2 s ((x,y)#next) = (if(s=x) then y else assoc2 s next)"

(* fun symTableMerge:: "symTable2 \<Rightarrow> symTable2 \<Rightarrow> symTable2"
  where
"symTableMerge [] [] = []" |
"symTableMerge t [] = t" |
"symTableMerge [] t = t" |
"symTableMerge (x#t1) t2 = (if(List.member t2 x) then 
        symTableMerge t1 t2 
      else 
        symTableMerge t1 (t2@[x]))"

fun symTableMergeCond:: "(symTable2 * bool) \<Rightarrow> (symTable2 * bool) \<Rightarrow> (symTable2 * bool)"
  where
"symTableMergeCond (t1,b1) (t2,b2) = ((symTableMerge t1 t2), (b1\<and>b2))"
*)


(* 
    Evaluation des expressions par rapport a une table de symboles 
      avec l'utilisation de Anyval
*)

fun evalE2:: "expression \<Rightarrow> symTable2 \<Rightarrow> Anyval"
where
"evalE2 (Constant s) e = (Myint s)" |
"evalE2 (Variable s) e = (case (assoc2 s e) of Myint(x) \<Rightarrow> (Myint x) | Any \<Rightarrow> Any)" |
"evalE2 (Sum e1 e2) e = (case (evalE2 e1 e, evalE2 e2 e) of
          (Any, _) \<Rightarrow> Any |
          (_, Any) \<Rightarrow> Any |
          (Myint(a), Myint(b)) \<Rightarrow> Myint(a+b))" |
"evalE2 (Sub e1 e2) e = (case (evalE2 e1 e, evalE2 e2 e) of
          (Any, _) \<Rightarrow> Any |
          (_, Any) \<Rightarrow> Any |
          (Myint(a), Myint(b)) \<Rightarrow> Myint(a-b))"

(* 
    Evaluation des conditions par rapport a une table de symboles 
      avec l'utilisation de Anyval
*)

fun evalC2:: "condition \<Rightarrow> symTable2 \<Rightarrow> (symTable2 * AnyCond)"
where
"evalC2 (Eq (Variable e1) e2) e = (case (evalE2 (Variable e1) e, evalE2 e2 e) of
          (Any, Myint(d)) \<Rightarrow> ((e1, Myint d)#e, AnyCond) |
          (Any, _) \<Rightarrow> (e, AnyCond) |
          (_, Any) \<Rightarrow> (e, AnyCond) |
          (Myint(a), Myint(b)) \<Rightarrow> ((e1, Myint a)#e, Mybool(a=b)))" |
"evalC2 (Eq e1 e2) e = (case (evalE2 e1 e, evalE2 e2 e) of
          (Any, _) \<Rightarrow> (e, AnyCond) |
          (_, Any) \<Rightarrow> (e, AnyCond) |
          (Myint(a), Myint(b)) \<Rightarrow> (e, Mybool(a=b)))"


(* Evaluation d'un programme par rapport a une table des symboles *)

fun san:: "statement \<Rightarrow> symTable2 \<Rightarrow>(symTable2 * bool)"
  where
"san (Aff s e)  t = ((s, (evalE2 e t))#t, True)" |
"san (Read s) t = ((s,Any)#t, True)" |
"san (If c s1 s2) t= 
(let (_, c1)= (evalC2 c t) in
  (case (c1) of
    AnyCond \<Rightarrow>  (t,False) |
    Mybool(x) \<Rightarrow> ( if(x) then 
        san s1 t
      else 
        san s2 t)))" |
"san (Seq s1 s2) t = 
  (let (t1, b1) = (san s1 t) in
    (let (t2, b2) = (san s2 t1) in 
      (t2, b1\<and>b2)))" |
"san (Exec e) t = 
    (case((evalE2 e t)) of
      Any \<Rightarrow> (t,False) |
      Myint(x) \<Rightarrow> (t,(x\<noteq>0)))"  |
"san _ t =  (t,True)"

lemma san:"let(t,b) = (san x []) in b  \<longrightarrow> (\<forall>b.\<not>BAD(evalS x ([],b,[])))"
  nitpick[timeout=30]
  apply (induct x)
  sorry


(* ----- Restriction de l'export Scala (Isabelle 2018) -------*)
(* ! ! !  NE PAS MODIFIER ! ! ! *)
(* Suppression de l'export des abstract datatypes (Isabelle 2018) *)
code_reserved Scala
  expression condition statement 
code_printing
   type_constructor expression \<rightharpoonup> (Scala) "expression"
  | constant Constant \<rightharpoonup> (Scala) "Constant"
  | constant Variable \<rightharpoonup> (Scala) "Variable"
  | constant Sum \<rightharpoonup> (Scala) "Sum"
  | constant Sub \<rightharpoonup> (Scala) "Sub"  

  | type_constructor condition \<rightharpoonup> (Scala) "condition"
  | constant Eq \<rightharpoonup> (Scala) "Eq"

  | type_constructor statement \<rightharpoonup> (Scala) "statement"
  | constant Seq \<rightharpoonup> (Scala) "Seq"
  | constant Aff \<rightharpoonup> (Scala) "Aff"
  | constant Read \<rightharpoonup> (Scala) "Read"
  | constant Print \<rightharpoonup> (Scala) "Print"
  | constant Exec \<rightharpoonup> (Scala) "Exec"
  | constant If \<rightharpoonup> (Scala) "If"
  | constant Skip \<rightharpoonup> (Scala) "Skip"
  | code_module "" \<rightharpoonup> (Scala) 



(* Directive pour l'exportation de l'analyseur *)

export_code san in Scala (case_insensitive)

(* file "~/workspace/TP67/src/tp67/san.scala"   (* à adapter en fonction du chemin de votre projet TP67 *)
*)

end
