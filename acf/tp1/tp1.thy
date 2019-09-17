theory tp1
imports Main
begin

lemma "A \<or> B"
nitpick
oops

lemma "A \<and> B \<longrightarrow> B"
apply auto
done

(* Exo 1 *)

lemma "(A \<and> (B \<or> C)) = ((A \<and> B) \<or> (A \<and> C))"
  apply auto
  done

lemma "(\<not>(A \<and> B)) = (\<not>A \<or> \<not>B)"
apply auto
done


(* Exo 2 *)

lemma "\<not>((\<not>E \<longrightarrow> C) \<and> (C \<longrightarrow> K) \<and> (M \<longrightarrow> \<not>D) \<and> (D = E) \<and> (K \<longrightarrow> (E \<and> M)) \<and> (E \<longrightarrow> K))"
  apply auto
  done

lemma "\<not>((C \<longrightarrow> K) \<and> (M \<longrightarrow> \<not>D) \<and> (D = E) \<and> (K \<longrightarrow> (E \<and> M)) \<and> (E \<longrightarrow> K))"
  nitpick
  oops

(* Exo3 *)

lemma "\<forall> (x::nat). (x + y > z + x) \<longrightarrow> (x + x > y + z)"
  nitpick
  oops

lemma "\<forall> (x::nat). \<not> ((x + y > z + x) \<longrightarrow> (x + x > y + z))"
  nitpick
  oops

lemma "\<forall> (x::nat). (x = y) \<and> (x + y > z + x) \<longrightarrow> (x + x > y + z)"
  apply auto
  done

lemma "\<forall> x::nat. x+y \<le> x*y"
  nitpick
  oops
lemma "\<forall> x::nat. x > 1 \<and> y > 1 \<longrightarrow> x+y \<le> x*y"
  nitpick
  oops

lemma "\<forall> x::nat . (x > y \<and> z > 0) \<longrightarrow> (x * z > y * z)"
  apply auto
  done

lemma "(\<exists>x::nat. P(f(x))) \<longrightarrow> (\<forall>x::nat. P(f(x)))"
  nitpick
  oops

(* Exo 4 *)

lemma "\<forall> a::nat. (a + b) = (b + a)"
  apply auto
  done

lemma "\<forall> a::nat. (a + (b + c)) = ((a + b) + c)"
  apply auto
  done

lemma "\<exists> x::nat. a + x = a"
  apply auto
  done

(* Exo 5 *)
lemma "append a b = append b a"
  nitpick
  oops

lemma "append a (append b c) = append (append a b) c"
  apply auto
  done

lemma "\<exists> x. append a x = a"
  apply auto
  done

(* Exo 6 *)
lemma "length (append a b) = length a + length b"
  apply auto
  done
lemma "map f (append a b) = append (map f a) (map f b)"
  apply auto
  done
lemma "((List.member a x) \<or> (List.member b x)) = (List.member (append a b) x)"
  nitpick
  sorry



end
