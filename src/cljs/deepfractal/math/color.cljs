(ns deepfractal.math.color
    (:require [deepfractal.math.point :as p]))

(defn interpolate [[x1 y1] [x2 y2] val]
  (if (= x1 x2)
    (* 0.5 (+ y1 y2))
    (+ y1 (/ (* (- y2 y1)
                (- val x1))
             (- x2 x1)))))


;; slow version, will need some optimization later
(defn value->color [path value]
  (if (nil? value)
    0
    (let [p (p/point value 0.5)
          above (first (subseq path >= p))
          below (first (rsubseq path <= p))
          c (cond
              (nil? above) (when-not (nil? below) (p/y (second below)))
              (nil? below) (p/y (second above))
              :default     (interpolate (second above) (second below) (p/x p)))]
      (.floor js/Math (* c 2.56)))))



;; root 2, 3
;; parent i (x), i+1 (y)
;; left child 2i, 2i+1
;; left child 2i+2, 2i+3

(defn make-tree! [tree coll]
  (letfn [(iter [tree-index coll-index-left coll-index-right]
            (let [coll-index-root (+ coll-index-left
                                     (quot (inc (- coll-index-right coll-index-left)) 2))
                  [x y] (aget coll coll-index-root)]
              (aset tree tree-index x)
              (aset tree (inc tree-index) y)
              (when (< coll-index-left coll-index-root)
                (iter (* 2 tree-index) coll-index-left (dec coll-index-root)))
              (when (< coll-index-root coll-index-right)
                (iter (* 2 (inc tree-index)) (inc coll-index-root) coll-index-right))))]
    (iter 2 0 (dec (.-length coll)))
    tree))

(defn priority-map->search-tree [pmap nil-color]
  (let [coll (clj->js (map (comp (fn [[x y]] #js [x y]) second)
                           (seq pmap)))
        tree (js/Array.)]
    (aset tree 1 nil-color)
    (make-tree! tree coll)))

(defn interpolate2 [x1 y1 x2 y2 val]
  (.floor js/Math
          (* 2.56
             (cond
               (nil? x1) y2
               (nil? x2) y1
               :default (+ y1 (/ (* (- y2 y1)
                                    (- val x1))
                                 (- x2 x1)))))))

(defn fast--value->color [tree value]
  (if (nil? value)
    (aget tree 1)
    (letfn [(tree-find [min-x min-y max-x max-y index]
              (let [x (aget tree index)
                    y (aget tree (inc index))]
                (if (nil? x)
                  (interpolate2 min-x min-y max-x max-y value)
                  (if (< value x)
                    (recur min-x min-y x y (* 2 index))
                    (recur x y max-x max-y (* 2 (inc index)))))))]
      (tree-find nil nil nil nil 2))))



(comment
  (def paths @(re-frame.core/subscribe [:color-editor-paths]))
  (def red (:red paths))

  (value->color red 110)




  (do
    (def pmap (into deepfractal.db/empty-path
                    (deepfractal.db/random-path 250 1000 100)))
    (def tree (time (priority-map->search-tree (:green paths))))

    (fast--value->color tree 1901.2 )
    )
  )
