(ns deepfractal.math.mandel
  (:require [deepfractal.utils :as utils]
            [deepfractal.math.fns :refer [limit] :as fns ]
            [deepfractal.math.arprec :as arprec]
            ))




(defn mandel-simple [max-n in-x in-y]
  (if (fns/trivially-inside? in-x in-y)
    nil
    (loop [n 0 x 0.0 y 0.0]
      (if (== n max-n)
        nil
        (let [x2 (* x x)
              y2 (* y y)
              d2 (+ x2 y2)]
          (if (> d2 limit)
            (fns/d2->n-value d2 n)
            (recur (inc n)
                   (+ x2 (- y2) in-x)
                   (+ (* 2 x y) in-y))))))))

(defn mandel-perturbation [reference-values max-n in-dx in-dy]
  (let [[ref-x ref-y] reference-values]
    (loop [n 0 dx 0.0 dy 0.0]
      (if (== n max-n)
        nil
        (let [tx (+ (aget ref-x n) dx)
              ty (+ (aget ref-y n) dy)
              d2 (+ (* tx tx) (* ty ty))]
          (if (> d2 limit)
            (fns/d2->n-value d2 n)
            (recur (inc n)
                   (+ (- (* tx dx) (* ty dy)) in-dx)
                   (+ (* tx dy) (* tx dy) in-dy))))))))

(defn mandel-arprec [max-n in-x in-y]
  (let [x-array #js []
        y-array #js []
        [x y diff sum] (repeatedly arprec/make)
        cathetus-limit (.sqrt js/Math (/ limit 2))
        limit (arprec/make limit)]
    (loop [n 0]
      (let [x_d (arprec/to_double x)
            y_d (arprec/to_double y)]
        (aset x-array n x_d)
        (aset y-array n y_d)
        (if (== n max-n)
          {:values max-n :data [x-array y-array]}
          (do
            (if (and (or (> (.abs js/Math x_d) cathetus-limit)
                         (> (.abs js/Math y_d) cathetus-limit))
                     (arprec/greater-than
                      (+ (arprec/sqr x) (arprec/sqr y))
                      limit))
              {:values n :data [x-array y-array]}
              (do
                (arprec/sub-set! diff x y)
                (arprec/add-set! sum x y)
                (arprec/mul-with! y x)
                (arprec/mul-with! y 2)
                (arprec/add-to! y in-y)
                (arprec/mul-set! x diff sum)
                (arprec/add-to! x in-x)
                (recur (inc n))))))))))

(def mandel mandel-simple)


(defn make-mandel-data [{:keys [width height]} [cx cy :as center] zoom max-n]
  (let [ds (/ 3.5 (* 0.5 (+ width height)) zoom)
        cx (utils/->float cx)
        cy (utils/->float cy)
        max-n (int max-n)
        a #js []]
    (dotimes [j height]
      (let [y (+ cy (* (- (/ (dec height) 2) j) ds))
            _ (aset a j #js [])
            aj (aget a j)]
        (dotimes [i width]
          (let [x (+ cx (* (- i (/ (dec width) 2)) ds))]
            (aset aj i (mandel max-n x y))))))
    a))

(comment

  (mandel-simple 10000 0 1)
  (mandel-simple 35 0 1.0000000001)
  (mandel-simple 36 0 1.0000000001)

  (time
   (let [x 0.000002342]
     (/ (* x (mandel-simple :no-limit -0.75 x))
        (.-PI js/Math))))

  (do
    (defn round2 [x]
      (* 0.01 (.round js/Math (* x 100))))

    (defn get-time! [] (.getTime (js/Date.)))

    (defn benchmark [label f n]
      (let [start (get-time!)
            ret (f n -0.1 0.8)
            ms (- (get-time!) start)
            Miter-per-s (round2 (/ n ms 1000))
            _ (prn )]
        (prn (str label ": " ms "ms, " Miter-per-s "M it./s"))
        Miter-per-s)))

  (do
    (time (def data (make-mandel-data {:width 1920 :height 1080}
                                      [-0.75 0] 1 1000)))
    nil)

  (time
   (let [canvas (.getElementById js/document "fractal-canvas")
         ctx (.getContext canvas "2d")
         dim {:width 329 :height 362}]
     (draw-image ctx dim {:center [-0.158 1.0335] :zoom 15 :max-n 10000})))

  (do
    (def n 30000000)

    (benchmark "Simple" mandel-simple n)
    ;; ~285M iterations/s

    (defn fill [a value]
      (if (.-fill a)
        (.fill a value)
        (dotimes [i (.-length a)]
          (aset ref-x i value))))

    (defn empty-array [n]
      (fill (new js/Array n) 0))

    (def ref [(empty-array n) (empty-array n)])

    (benchmark "Perturbation" (partial mandel-perturbation ref) n)
    ;; ~238M iterations/s
    )


  (do
    (arprec/set-precision 1000000)
    (time (def ref (mandel-arprec 100 (arprec/make 0.1) (arprec/make 0.4))))
    ;; ~380k it/s for precision 100
    ;;  ~28k it/s for precision 1000
    ;; ~1.1k it/s for precision 10000
    ;;  ~120 it/s for precision 100000
    ;;   ~11 it/s for precision 1000000 (compile arprec with -s TOTAL_MEMORY=134217728)
    nil
    )
  )
