(ns deepfractal.math.fns)

(defn sqr [x] (* x x))

(defn ** [base exponent]
  (.pow js/Math base exponent))

(def limit (** 2 (** 2 5)))

(defn d2->n-value [d2 n]
  (- (+ n 5) (.log2 js/Math (* 0.5 (.log2 js/Math d2)))))

(defn trivially-inside? [x y]
  (let [xm (- x 0.25)
        r2 (+ (sqr xm) (sqr y))
        xp (+ x 1.0)
        d2 (+ (sqr xp) (sqr y))]
    (or
     (< (sqr (+ xm (* 2 r2))) r2) ; inside cardioid
     (< d2 0.0625)                ; inside left circle
     )))
