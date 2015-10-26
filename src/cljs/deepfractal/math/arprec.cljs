(ns deepfractal.math.arprec)


(defn make
  ([] (new js/Module.mp_real "0e0"))
  ([s] (new js/Module.mp_real (str s))))

(defn to-string [x]
  (.to_string js/Module x))

(defn mp_real? [x]
  (instance? js/Module.mp_real x))

(defn set-precision [precision]
  (.set_precision js/Module precision)

  (extend-type js/Module.mp_real
    Object
    (toString [this] (to-string this))

    IPrintWithWriter
    (-pr-writer [this writer _]
      (-write writer (str this)))

    IComparable
    (-compare [x y]
      (cond
        (mp_real? y) (.compare js/Module x y)
        (or (number? y)
            (string? y)) (.compare js/Module x (make y))
        :default (throw (js/Error. (str "Cannot compare " x " to " y))))))

  precision)

(defn greater-than [x y]
  (== (compare x y) 1))

(defn index [x n]
  (.index x n))

(defn to_double [x]
  (.to_double js/Module x))

;; Addition

(defn add-to! [x y]
  (if (mp_real? y)
    (.add js/Module x x y)
    (.add_d js/Module x x y))
  x)

(defn add-set! [res x y]
  (if (mp_real? y)
    (.add js/Module res x y)
    (.add_d js/Module res x y))
  res)

(defn add
  ([] (make))
  ([x] x)
  ([x y]
   (add-set! (make) x y))
  ([x y & more]
   (reduce add-to! (add x y) more)))

;; Subtraction

(defn sub-from! [x y]
  (if (mp_real? y)
    (.sub js/Module x x y)
    (.sub_d js/Module x x y))
  x)

(defn sub-set! [res x y]
  (if (mp_real? y)
    (.sub js/Module res x y)
    (.sub_d js/Module res x y))
  res)

(defn sub
  ([] (make))
  ([x] x)
  ([x y]
   (sub-set! (make) x y))
  ([x y & more]
   (reduce sub-from! (sub x y) more)))

;; Multiplication

(defn mul-with! [x y]
  (if (mp_real? y)
    (.mul js/Module x x y)
    (.mul_d js/Module x x y 0))
  x)

(defn mul-set! [res x y]
  (if (mp_real? y)
    (.mul js/Module res x y)
    (.mul_d js/Module res x y 0))
  res)

(defn mul
  ([] (make "1e0"))
  ([x] x)
  ([x y]
   (mul-set! (make) x y))
  ([x y & more]
   (reduce mul-with! (mul x y) more)))

;; Division

(defn div-with! [x y]
  (if (mp_real? y)
    (.div js/Module x x y)
    (.div_d js/Module x x y 0))
  x)

(defn div-set! [res x y]
  (if (mp_real? y)
    (.div js/Module res x y)
    (.div_d js/Module res x y 0))
  res)

(defn div
  ([] (make "1e0"))
  ([x] x)
  ([x y]
   (div-set! (make) x y))
  ([x y & more]
   (reduce div-with! (div x y) more)))

;; Square and Square root

(defn sqr-set! [res x]
  (.sqr js/Module res x)
  res)

(defn sqr [x]
  (sqr-set! (make) x))

(defn sqrt-set! [res x]
  (.sqrt js/Module res x)
  res)

(defn sqrt [x]
  (sqrt-set! (make) x))

;; Exponentials and logarithms

(defn exp-set! [res x]
  (.exp js/Module res x)
  res)

(defn exp [x]
  (exp-set! (make) x))

(defn log-set! [res x]
  (.log js/Module res x)
  res)

(defn log [x]
  (log-set! (make) x))

(defn log10-set! [res x]
  (.log10 js/Module res x)
  res)

(defn log10 [x]
  (log10-set! (make) x))

;; Trigonometry

(defn sin-set! [res x]
  (.sin js/Module res x)
  res)

(defn sin [x]
  (sin-set! (make) x))

(defn cos-set! [res x]
  (.cos js/Module res x)
  res)

(defn cos [x]
  (cos-set! (make) x))

(defn sincos [x]
  (let [s (make)
        c (make)]
    (.sincos js/Module s c x)
    [s c]))

(defn tan-set! [res x]
  (.tan js/Module res x)
  res)

(defn tan [x]
  (tan-set! (make) x))

(defn asin-set! [res x]
  (.asin js/Module res x)
  res)

(defn asin [x]
  (asin-set! (make) x))

(defn acos-set! [res x]
  (.acos js/Module res x)
  res)

(defn acos [x]
  (acos-set! (make) x))

(defn atan-set! [res x]
  (.atan js/Module res x)
  res)

(defn atan [x]
  (atan-set! (make) x))

(defn atan2-set! [res x y]
  (.atan2 js/Module res x y)
  res)

(defn atan2 [x y]
  (atan2-set! (make) x y))

(comment
  (let [_ (init 100000)
        y (make "1.23e1")
        ys (time (sqrt y))
        _ (print ys)
        _ (time (*= ys ys))
        _ (print ys)])


  )
