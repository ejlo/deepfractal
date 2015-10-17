(ns deepfractal.utils)

(def debounce-timers (atom {}))

(defn clear-timer! [key]
  (when-let [timeout (@debounce-timers key)]
    (js/clearTimeout timeout)
    (swap! debounce-timers dissoc key)))

(defn debounce [key ms f]
  (clear-timer! key)
  (swap! debounce-timers assoc key
         (js/setTimeout #(do (clear-timer! key) (f)) ms)))

(defn round-to-significant-figures [num n]
  (if (zero? num)
    0
    (let [d (->> num (.abs js/Math) (.log10 js/Math))
          power (.floor js/Math (- n d))
          magnitude (.pow js/Math 10 power)
          shifted (.round js/Math (* num magnitude))]
      (/ shifted magnitude))))

(defn ->float [x]
  (let [x (if (and x (number? x)) x (js/parseFloat x))]
    (if (js/isNaN x) 0 x)))


(defn get-time! [] (.getTime (js/Date.)))

(defn benchmark [label f n]
  (prn "Benchmark starting!")
  (let [start (get-time!)
        ret (f n)
        ms (- (get-time!) start)
        Miter-per-s (round-to-significant-figures (/ n ms 1000) 2)
        _ (prn )]
    (prn (str label ": " ms "ms, " Miter-per-s "M it./s"))
    Miter-per-s))

(defn by-id [id]
  (.getElementById js/document id))
