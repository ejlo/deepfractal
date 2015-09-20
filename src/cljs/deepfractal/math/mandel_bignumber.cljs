(ns deepfractal.math.mandel-bignumber
  (:refer-clojure :exclude [+ - * /])
  (:require [deepfractal.utils :as utils]
            [deepfractal.math.bignumber :refer [+ - * /]]
            [deepfractal.math.fns :as mandel :refer [limit d2->n-value]
             ])
  )

(defn big [val]
  (new js/Decimal val))

(defn set-precision! [digits]
  (.config js/Decimal #js {:precision digits}))

(defn big->float [x]
  (js/parseFloat (.toPrecision x 20)))

(defn mandel-simple [max-n in-x in-y]
  (loop [n 0 x (big 0.0) y (big 0.0)]
      (if (== n max-n)
        nil
        (let [x2 (* x x)
              y2 (* y y)
              d2 (+ x2 y2)]
          #_(do (prn (str "n: " n))
              (prn (str  "[x,y]: [" (.valueOf x) ", " (.valueOf y) "]"))
              (prn (str  "[x2,y2]: [" (.valueOf x2) ", " (.valueOf y2) "]"))
              (prn (str  "d2: " (.valueOf d2))))
          (if (> d2 limit)
            (d2->n-value (big->float d2) n)
            (recur (inc n)
                   (+ (- x2 y2) in-x)
                   (let [xy (* x y)]
                     (+ (+ xy xy) in-y)))))))
   )

(comment

  (set-precision 100)
  (mandel-simple 1000 (big 0) (big 1))
  (mandel-simple 35 (big 0) (big 1.0000000001))
  (mandel-simple 36 (big 0) (big 1.0000000001))
  (time (do
          (set-precision 1000)
          (mandel-simple 10000 (big 0) (+ (big "1e-998") (big 1)))))

  (let [_ (set-precision! 500)
        x (.sqrt (big 2))]
    (time (dotimes [n 100]
            (* x x)))
    )

  (do
    (def n 1000)
    (set-precision! 100)
    (utils/benchmark "BigNumber Simple"
                     #(mandel-simple % (big -0.1) (big 0.8)) n)
    ;; ~15k iterations/s @ precision 100
    ;; ~210 iterations/s @ precision 1000
    )


  )
