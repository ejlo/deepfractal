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
