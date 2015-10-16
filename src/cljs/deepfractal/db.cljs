(ns deepfractal.db
  (:require [tailrecursion.priority-map :refer [priority-map-by]]
            [deepfractal.math.point :as g]))

(defn random-points [n x y]
  (for [i (range n)]
    [-i (g/point (rand-int x) (rand-int y))]))

(def empty-path (priority-map-by
                 #(let [c (compare (g/x %1) (g/x %2))]
                    (if (zero? c)
                      (compare (g/y %1) (g/y %2) )
                      c))))

(defn random-path []
  (into empty-path (random-points 25 1680 280)))

(defonce random-paths
  {:red (random-path)
   :green (random-path)
   :blue (random-path)
   :alpha (random-path)})

(def default-db
  {:canvas-size {:width 320 :height 200}
   :fractal-params {:center [-0.75 0]
                    :max-n 1000
                    :zoom 1}
   :color-editor {:paths random-paths}})
