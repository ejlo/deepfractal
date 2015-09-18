(ns fractal.math.coords)

;; canvas coords (i,j)
;;
;; (0,0)
;;  ----------------
;;  |              |
;;  |              |
;;  |              |
;;  ----------------
;;           (width,height)
;;
;;
;;
;;
;; fractal coords (x,y)
;;
;;  ----------------
;;  |              |
;;  |    (cx,cy)   |
;;  |              |
;;  ----------------
;; width: ~1/zoom


(defn canvas->fractal [{:keys [width height]} [cx cy] zoom [i j]]
  (let [ds (/ 3.5 (* 0.5 (+ width height)) zoom)]
    [(+ cx (* (- i (/ (dec width) 2)) ds))
     (+ cy (* (- (/ (dec height) 2) j) ds))]))

(defn fractal->canvas [{:keys [width height]} [cx cy] zoom [x y]]
  (let [ds (/ 3.5 (* 0.5 (+ width height)) zoom)]
    [(+ (/ (dec width) 2) (/ (- x cx) ds))
     (- (/ (dec height) 2) (/ (- y cy) ds))]))

(defn zoom [[cx cy] [x y] zoom-factor]
  [(- x (/ (- x cx) zoom-factor))
   (- y (/ (- y cy) zoom-factor))])
