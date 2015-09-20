(ns deepfractal.math.bignumber
  (:refer-clojure :exclude [+ - * /]))

(defn + [x y]
  (.plus x y))

(defn - [x y]
  (.minus x y))

(defn * [x y]
  (.times x y))

(defn / [x y]
  (.dividedBy x y))
