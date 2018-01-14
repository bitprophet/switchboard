(ns switchboard.t-core
  (:require [switchboard.utils :refer :all]
            [switchboard.t-utils :refer :all]
            [midje.sweet :refer :all]))


(defn goog [x] (redirect (str "https://google.com/search?q=" x)))


(fact "lack of query param displays error"
      (request {}) => error-response)

(fact "present but empty query param value displays error"
      (query "") => error-response)

(fact "if no submodule is matched, default is to Google"
      (query "nope") => (goog "nope")
      (query "nope nohow") => (goog "nope nohow"))

(fact "gis searches google image search"
      (query "gis") => (redirect "https://images.google.com/")
      (query "gis apple") => (redirect "https://www.google.com/search?tbm=isch&q=apple")
      (query "gis apple pie") => (redirect "https://www.google.com/search?tbm=isch&q=apple pie"))

(fact "ann searches Anime News Network"
      (query "ann") => (redirect "http://www.animenewsnetwork.com")
      (query "ann sword art online") => (redirect "http://www.animenewsnetwork.com/encyclopedia/search/name?q=sword art online"))

(fact "fft prefixes with 'Final Fantasy Tactics'"
      ;; Huge and useful mechanics guide is as good a base as any
      (query "fft") => (redirect "https://www.gamefaqs.com/ps/197339-final-fantasy-tactics/faqs/3876")
      ;; Normally, search Google for FFT specific content (including attempts
      ;; to filter out A/A2/etc)
      (query "fft ramza") => (goog "\"final fantasy tactics\" -\"grimoire\" -\"tactics advance\" ramza"))
