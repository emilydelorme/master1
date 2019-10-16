theory tp3Bis
imports Main 
begin

(* Exploitez les générateurs de contre-exemples (nitpick/quickcheck) pour trouver des contre-exemples
  sur les lemmes des exercices 1,2,3 et 4 *)

(* --------- exercice 1 ---------------- *)
(* Avec quickcheck et nitpick *)
lemma exo1: "([x,y,z] @ [z,y,x]) = ([z,y,x] @ [x,y,z])"
  quickcheck
  nitpick
oops

(* --------- exercice 2 ---------------- *)
fun member::"'a => 'a list => bool"
where 
"member _ [] = False" |
"member e (x # xs) = (if (x=e) then True else (member e xs))"

fun oneDelete:: "'a => 'a list => 'a list"
where 
"oneDelete e [] = []" |
"oneDelete e (a#l) = (if e=a then l else a#(oneDelete e l))"

fun isPermut:: "'a list \<Rightarrow> 'a list \<Rightarrow> bool"
where
"isPermut [] [] = True" |
"isPermut (_#_) [] = False" |
"isPermut [] (_#_) = False" |
"isPermut (x#xs) l = (if (member x l) then (isPermut xs (oneDelete x l)) else False)"

(* Avec quickcheck et nitpick *)
lemma exo2: "\<forall> l1. (((length l1) = (length l2)) \<and> (length l1 >10) \<and> (isPermut l1 l2)) \<longrightarrow> (isPermut (oneDelete x l1) (oneDelete y l2))"
  quickcheck [tester=narrowing, size=11]
  oops

(* --------- exercice 3 ---------------- *)
(* Contrexemples avec quickcheck, nitpick n'y parvient pas dans un temps raisonnable *)
lemma exo3: "((x>4)\<and>(y>30)\<and>(z>12)) \<longrightarrow> (((x::nat)*y)+(y*z) \<noteq> (x*z)+(z*y))"
   quickcheck [tester=narrowing, size=31]
oops


(* --------- exercice 4 ---------------- *)

fun cutFirst:: "'a list \<Rightarrow> nat \<Rightarrow> 'a list"
where
"cutFirst [] _= []" |
"cutFirst (x#_) (Suc 0)= [x]" |
"cutFirst (x#xs) y = x#(cutFirst xs (y - 1))"

fun cutEnd:: "'a list \<Rightarrow> nat \<Rightarrow> 'a list"
where
"cutEnd [] _ = []" |
"cutEnd (x#xs) (Suc 0)= xs" |
"cutEnd (x#xs) y = (cutEnd xs (y - 1))"


(* Ce lemme n'admet PAS de contre exemple, il est juste nécessaire pour la suite *)
lemma cutEnd_decreasing: "l\<noteq>[] \<Longrightarrow> length(cutEnd l i) < (length l)"
sorry


function slice:: "'a list \<Rightarrow> ('a list) list"
where
"slice l1= 
  (if l1=[] then [] else 
      (let deb=(cutFirst l1 19) in
        let fin=(cutEnd l1 19) in
            (deb#(slice fin))))"
by pat_completeness auto
termination slice
apply (relation "measure (%i. (length i))")
apply auto
by (metis cutEnd_decreasing)


(* --------- exercice 4 ---------------- *)
(* Avec quickcheck et nitpick *)
lemma exo4: "length(slice l)<=1"
     quickcheck [tester=narrowing, size=20]
oops



end