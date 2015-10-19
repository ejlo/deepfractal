(ns deepfractal.db
  (:require [tailrecursion.priority-map :refer [priority-map]]
            [deepfractal.math.point :as g]))

(defn random-points [n x y]
  (for [i (range n)]
    [(- i) (g/point (rand-int x) (rand-int y))]))

(def empty-path (priority-map))

(defn random-path [n max-x max-y]
  (into empty-path (random-points n max-x max-y)))

(def default-points
  {:red [[0 0] [6 0] [75 100]]
   :green [[0 0] [6 0] [65 100]]
   :blue [[0 0] [6 0] [20 100] [65 20] [100 100]]
   :alpha [[0 100] [1000 100]]})

(defonce random-paths
  (let [r #(random-path 5 1000 100)]
    {:red (r)
     :green (r)
     :blue (r)
     :alpha (r)}))

(defn coords->points [coords start-index]
  (->> coords
       (mapv #(apply g/point %))
       (#(for [i (range (count %)) :let [p (nth % i)]]
           [(+ i start-index) p]))))

(defonce default-paths
  (first
   (reduce-kv (fn [[coll idx] k v]
                [(assoc coll k (into empty-path (coords->points v idx)))
                 (+ idx (count v))])
              [{} -1000]
              default-points)))

(def default-db
  {:canvas-size {:width 320 :height 200}
   :fractal-params {:center [-0.75 0]
                    :max-n 1000
                    :zoom 1}
   :color-editor {:paths default-paths
                  :bcr nil}})
