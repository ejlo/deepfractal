(ns deepfractal.math.point)

(defprotocol IPoint
  (x [p])
  (y [p]))

(deftype Point [x-coord y-coord]
  IPoint
  (x [_] x-coord)
  (y [_] y-coord)

  Object
  (toString [_] (str "#<" x-coord ", " y-coord ">"))

  IComparable
  (-compare [_ other]
    (let [c (compare x-coord (x other))]
      (if (zero? c)
        (compare y-coord (y other))
        c)))

  IPrintWithWriter
  (-pr-writer [_ writer _]
    (-write writer (str "#<" x-coord ", " y-coord ">")))

  IIndexed
  (-nth [p n]
      (nth p n nil))
  (-nth [p n not-found]
    (cond
      (= n 0) (x p)
      (= n 1) (y p)
      :default not-found))
  )

(defn point [x y]
  (Point. x y))

(defn dist [p1 p2]
  (js/Math.sqrt (+ (js/Math.pow (- (x p2) (x p1)) 2)
                   (js/Math.pow (- (y p2) (y p1)) 2))))
