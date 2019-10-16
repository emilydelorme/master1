theory tp2
  imports Main
begin

fun member::"'a \<Rightarrow> 'a list \<Rightarrow> bool"
  where
"member _ [] = False" |
"member e (x # xs) = (if (e=x) then True else (member e xs))"

fun isSet::"'a list \<Rightarrow> bool"
  where
    "isSet [] = True"
  | "isSet (x # xs) = (\<not> (member x xs) \<and> (isSet xs))"

fun clean::"'a list \<Rightarrow> 'a list"
  where
    "clean [] = []"
  | "clean (x # xs) = (if (member x xs) then (clean xs) else x # (clean xs) )"

lemma "member x l \<longleftrightarrow> member x (clean l)"
  nitpick
  sorry

lemma "isSet (clean l)"
  nitpick
  sorry

fun delete::"'a \<Rightarrow>'a list \<Rightarrow> 'a list"
  where
  "delete _ [] = []" |
  "delete e (x # xs) = (if(e = x) then xs else x # (delete e xs))"

lemma "isSet l \<longrightarrow> \<not>member x (delete x l)"
  nitpick
  sorry

lemma "isSet l \<and> member x1 l \<and> x1 \<noteq> x2 \<longrightarrow> member x1 (delete x2 l)"
  nitpick
  sorry

fun intersection::"'a list \<Rightarrow> 'a list \<Rightarrow> 'a list"
  where
    "intersection _ [] = []"
  | "intersection l (x # xs) = (if (member x l) then x # (intersection l xs) else (intersection l xs))"

lemma "member x l1 \<and> member x l2 \<longleftrightarrow> member x (intersection l1 l2)"
  nitpick
  sorry

lemma "isSet l1 \<and> isSet l2 \<longrightarrow> isSet (intersection l1 l2)"
  nitpick
  sorry

fun union::"'a list \<Rightarrow> 'a list \<Rightarrow> 'a list"
  where
    "union l [] = l" |
    "union l (x # xs) = (if (member x l) then (union l xs) else x # (union l xs))"

lemma "member x l1 \<or> member x l2 \<longleftrightarrow> member x (union l1 l2)"
  nitpick
  sorry

lemma "isSet l1 \<and> isSet l2 \<longrightarrow> isSet (union l1 l2)"
  nitpick
  sorry

fun equal::"'a list \<Rightarrow> 'a list \<Rightarrow> bool"
  where
  "equal [] [] = True" |
  "equal l [] = False" |
  "equal l (x # xs) = (if(member x l) then (equal (delete x l) xs) else False)"

lemma "isSet l1 \<and> isSet l2 \<longrightarrow> equal l1 l2 \<longleftrightarrow> (\<forall>x.(member x l1) \<longleftrightarrow> (member x l2))"
  nitpick
  sorry




end