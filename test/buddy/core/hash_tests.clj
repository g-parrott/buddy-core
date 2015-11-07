;; Copyright 2014-2015 Andrey Antukh <niwi@niwi.nz>
;;
;; Licensed under the Apache License, Version 2.0 (the "License")
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns buddy.core.hash-tests
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [buddy.core.codecs :as codecs :refer :all]
            [buddy.core.bytes :as bytes]
            [buddy.core.keys :refer :all]
            [buddy.core.hash :as hash]
            [clojure.java.io :as io]))

(deftest buddy-core-hash
  (testing "SHA3 256"
    (let [hashed (hash/sha3-256 "")]
      (is (= (bytes->hex hashed)
             (str "a7ffc6f8bf1ed76651c14756a061d662"
                  "f580ff4de43b49fa82d80a4b80f8434a")))))

  (testing "SHA3 384"
    (let [hashed (hash/sha3-384 "")]
      (is (= (bytes->hex hashed)
             (str "0c63a75b845e4f7d01107d852e4c2485"
                  "c51a50aaaa94fc61995e71bbee983a2a"
                  "c3713831264adb47fb6bd1e058d5f004")))))

  (testing "SHA3 512"
    (let [hashed (hash/sha3-512 "")]
      (is (= (bytes->hex hashed)
             (str "a69f73cca23a9ac5c8b567dc185a756e"
                  "97c982164fe25859e0d1dcc1475c80a6"
                  "15b2123af1f5f94c11e3e9402c3ac558"
                  "f500199d95b6d3e301758586281dcd26")))))

  (testing "SHA2 256"
    (let [hashed (hash/sha256 "")]
      (is (= (bytes->hex hashed)
             (str "e3b0c44298fc1c149afbf4c8996fb924"
                  "27ae41e4649b934ca495991b7852b855")))))

  (testing "SHA2 384"
    (let [hashed (hash/sha384 "")]
      (is (= (bytes->hex hashed)
             (str "38b060a751ac96384cd9327eb1b1e36a"
                  "21fdb71114be07434c0cc7bf63f6e1da"
                  "274edebfe76f65fbd51ad2f14898b95b")))))

  (testing "SHA2 512"
    (let [hashed (hash/sha512 "")]
      (is (= (bytes->hex hashed)
             (str "cf83e1357eefb8bdf1542850d66d8007"
                  "d620e4050b5715dc83f4a921d36ce9ce"
                  "47d0d13c5d85f2b0ff8318d2877eec2f"
                  "63b931bd47417a81a538327af927da3e")))))

  (testing "blake2 512"
    (let [hashed (hash/blake2b-512 "")]
      (is (= (str/upper-case (bytes->hex hashed))
             (str "786A02F742015903C6C6FD852552D272"
                  "912F4740E15847618A86E217F71F5419"
                  "D25E1031AFEE585313896444934EB04B"
                  "903A685B1448B755D56F701AFE9BE2CE")))))

  (testing "sha1"
    (let [hashed (hash/sha1 "")]
      (is (= (bytes->hex hashed)
             "da39a3ee5e6b4b0d3255bfef95601890afd80709"))))

  (testing "File hashing"
    (let [path "test/_files/pubkey.ecdsa.pem"
          stream (io/input-stream path)]
      (is (= (bytes->hex (hash/sha256 stream))
             (str "7aa01e35e65701c9a9d8f71c4cbf056a"
                  "cddc9be17fdff06b4c7af1b0b34ddc29")))))

  (testing "low-level api"
    (let [engine (org.bouncycastle.crypto.digests.SHA1Digest.)]
      (hash/update! engine (str->bytes ""))
      (let [data (hash/end! engine)]
        (is (= (bytes->hex data)
               "da39a3ee5e6b4b0d3255bfef95601890afd80709"))))))


