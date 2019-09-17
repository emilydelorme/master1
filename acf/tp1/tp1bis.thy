theory tp1bis
imports Main
begin

(* Le but de ce TP est de manipuler les preuves en Isabelle/HOL et de:
   - s'habituer à lire les buts Isabelle de la forme a \<Longrightarrow> b \<Longrightarrow> c \<Longrightarrow> d, d'y retrouver les hypothèses 
     et la conclusion. 
   - comprendre qu'auto (ou simp) ne fera rien sur un appel de fonction (f x y), en particulier 
     il n'appliquera pas la définition de f si celle-ci est une définition par cas et a besoin d'avoir des 
     informations sur x et y pour pouvoir être simplifiée.
   - montrer comment faire une preuve par cas sur les paramètres x et y avec case_tac.
   - montrer comment ajouter des hypothèses supplémentaires à un but avec case_tac.
   - faire quelques preuves avec try0 et sledgehammer.
*)

(* D'abord quelques exemples *)

(* <<case_tac>>, pour:
  - faire une preuve par cas sur une variable de type inductif définie par "datatype": listes, nat, etc.
  - déplier la définition d'une variable définie par "type_synonym"
  - faire une preuve par cas sur une propriété
*)

lemma "(x::nat) \<noteq> 0"
  apply (case_tac x)     (* On fait une preuve par cas sur x: soit x=0 soit x=Suc nat, où nat est une nouvelle 
                            variable dont le nom est généré par Isabelle/HOL *)
  oops

type_synonym tuple= "nat*nat*nat"

lemma "(t::tuple)\<noteq> (0,0,0)"
  apply (case_tac t)     (* on déplie la définition du type tuple à l'aide de case_tac t *) 
  apply simp             (* on simplifie la formule en remplaçant t par sa valeur (a,b,c) *)
  oops

(* <<rename_tac>>, pour renommer une variable dont le nom a été généré par isabelle/HOL, pour:
  - avoir des identifiants plus lisibles
  - avoir des preuves plus robustes (d'une version à l'autre de Isabelle/HOL, le nommage des variables générées
    peut changer)
*)

lemma "(t::tuple)\<noteq> (0,0,0)"
  apply (case_tac t)    
  apply (rename_tac premier deuxieme troisieme)   (* On remplace les noms générés par Isabelle, par des noms plus explicites *)
  apply (case_tac premier)                        (* On fait une preuve par cas sur "premier", qui génère 2 sous-buts *)
   apply (case_tac [1-] deuxieme)                  (* On fait la même opération sur "deuxieme" sur TOUS les sous-buts *)
  apply (case_tac [2-4] troisieme)                  (* On fait la même opération sur "troisieme" sur les sous-buts 2 à 4 *)
  oops

lemma "(t::tuple)\<noteq> (0,0,0)"
  apply (case_tac t)    
  apply (rename_tac deuxieme troisieme)   (* rename_tac remplace les noms en commençant par les noms situés le plus à droite du symbole \<And> *)
  oops

(* <<try0>>, pour dépasser ce que sait faire auto sur des buts simples qui nécessitent des
   preuves par cas triviales et/ou la simplification de quantificateurs. Il faut que toutes les hypothèses
   nécessaires soient dans le but courant. En particulier, try0 ne sait pas utiliser de lemmes intermédiaires. 
   Pour cela voir plus bas: sledgehammer.
 *)

lemma "\<exists> (t1::tuple) (t2::tuple). t1\<noteq>(0,0,0) \<and> t2\<noteq>t1 \<and> t2\<noteq>(0,0,0)"
  apply auto  (* n'y arrive pas *)
  sorry

lemma "\<exists> (t1::tuple) (t2::tuple). t1\<noteq>(0,0,0) \<and> t2\<noteq>t1 \<and> t2\<noteq>(0,0,0)"
  try0         (* Quand try0 trouve une preuve, il suffit de double cliquer sur celle-ci pour l'intégrer *)
  by blast


(* <<case_tac>>, pour:
  - ajouter une hypothèse (démontrable) supplémentaire pendant une preuve
*)

(* Si on souhaite prouver cette propriété *)
lemma "\<exists> (x::nat) y. x+y > x*y"
  apply auto
  try0      
(* auto et try0 échouent. Pourtant la propriété est clairement vraie pour x=2 et y=1 par exemple  *)
  sorry


(* Recommençons. On fait la preuve en ajoutant une propriété intermédiaire qu'il va pouvoir exploiter *)
lemma "\<exists> (x::nat) y. x+y > x*y"
(* On lui donne une propriété P (qui doit être valide) pour les valeurs de x=2 et de y=1 *)
  apply (case_tac "(2::nat) + 1 > 2 * 1")
    (* avec (case_tac "P") on se retrouve a trouver P \<Longrightarrow> Q et \<not>P \<Longrightarrow> Q, 
       où Q= (\<exists> (x::nat) y. x+y > x*y) *)
  try0
  apply blast   (* Avec cette propriété Q est démontrable facilement *)
  apply simp   (* La propriété \<not>P est trivialement contradictoire *)
  done


(* Par sécurité, il vaut mieux s'assurer d'abord que P est valide! 
   On peut faire un apply (case_tac "\<not>P") pour commencer par montrer 
   que \<not>P est contradictoire. *)

(* La preuve précédente devient donc: *)
lemma "\<exists> (x::nat) y. x+y > x*y"
  apply (case_tac "\<not>((2::nat) + 1 > 2 * 1)") (* Avec cela on commence par prouver que \<not>P 
                                                est contradictoire, où P valide. *)
   apply simp   (* La propriété \<not>P est trivialement contradictoire *)
  apply blast   (* Avec P en hypothèse, la propriété Q est démontrable facilement *)
  done


(* Cependant, parfois faire un (case_tac "\<not>P") peut compliquer inutilement la preuve. 
   Une alternative est de conserver (case_tac "P") mais de traiter le cas \<not>P en premier. *)

(* <<prefer i>> 
   Choisir de commencer la preuve par un sous-but particulier
*)

(* La preuve devient: *)
lemma "\<exists> (x::nat) y. x+y > x*y"
   apply (case_tac "(2::nat) + 1 > 2 * 1")
   prefer 2   (* On commence par le cas \<not>P \<Longrightarrow> Q *)
   apply simp   (* La propriété \<not>P est trivialement contradictoire *)
  apply blast   (* Avec P en hypothèse, la propriété Q est démontrable facilement *)
  done

(* <<sledgehammer>> pour dépasser ce que savent faire auto/try sur des buts simples qui nécessitent d'utiliser
   des lemmes intermédiaires et/ou des lemmes issus des librairies d'Isabelle/HOL (sur les listes, nat, etc.)

   Pour utiliser sledgehammer le plus simple est de:
   - dans la preuve, placer le curseur là où l'on souhaite chercher une preuve
   - cliquer sur l'onglet "sledgehammer" dans le coin inférieur gauche de l'IDE Isabelle,
   - cliquer sur "Apply" 
   - quand sledgehammer trouve une preuve, il suffit de double cliquer sur celle-ci pour l'intégrer 
*)


(* Par exemple, si on souhaite prouver des propriétés sur les listes, sledgehammer saura retrouver 
   dans la librairie les lemmes nécessaires à la preuve. *)

(* Toujours cette même preuve. En fait, sledgehammer sait la faire tout seul, en se servant des lemmes 
   intermédiaires développés sur la librairie des nat *)
lemma "\<exists> (x::nat) y. x+y > x*y"
  by (metis less_add_same_cancel1 one_power2 power2_eq_square zero_less_one)

(* Définissons une fonction double, qui répète 2 fois les éléments d'une liste *)
fun double:: "'a list \<Rightarrow> 'a list"
  where
"double [] = []" |
"double (x#r) = (x#(x#(double r)))"

value "double [1::nat,2,3]"

(* Prouvons une première propriété sur cette fonction. Vous pouvez oublier la preuve de cette
   propriété pour le moment *)

lemma "x\<noteq>[] \<longrightarrow> length (double x) > length x"
  apply (induct x)
   apply auto
  done

(* Prouvons maintenant cette autre propriété *)

lemma "length (double (x#y)) \<noteq> length (x#y)"
  (* apply auto
  try0
  sledgehammer échouent. Alors que la propriété est une conséquence 
  presque directe du lemme au dessus *) 
  sorry

(* Si on NOMME le premier lemme *)
lemma length_double: "x\<noteq>[] \<longrightarrow> length (double x) > length x"
  apply (induct x)
   apply auto
  done

(* La preuve est trouvée par Sledgehammer *)

lemma "length (double (x#y)) \<noteq> length (x#y)"
  by (metis length_double list.discI nat_less_le)



(* -------------------- A vous de jouer: prouvez tous les lemmes ci-dessous. --------------------- *)


fun f::"tuple \<Rightarrow> nat"
where
"f (x,y,z) = (if x=0 then 1 else (if y>10 then 2 else (if z<4 then 18 else z+10)))"

lemma "f t \<noteq> 5" 
  done

fun g::"tuple \<Rightarrow> nat"
where
"g (0,_,_) = 1" |
"g (_, Suc 0, _) = 2" |
"g (_,_,m) = m+10" 

lemma "g t \<noteq> 5"  
  done


lemma "\<exists> l. ((length l)>2 \<and> (rev l = l))"
  done
  

(* Formalisez et démontrez les propriétés suivantes:

  - il existe une fonction de type nat \<Rightarrow> nat qui n'est pas inversible
  - si la fonction ff est inversible alors map ff l'est aussi
  - la fonction double est inversible  (on peut définir la fonction inverse   
    au préalable avec "fun")
*)

end
