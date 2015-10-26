#include <arprec/mp_real.h>
#include <iostream>
#include <iomanip>
#include <string>
#include <emscripten/bind.h>

// Compile lib:
// emconfigure ./configure --disable-assembly --build none
// emconfigure make -j8
//
// Compile this file:
// emcc -O3 --closure 0 --memory-init-file 0 --bind -s ASSERTIONS=1 -s NO_EXIT_RUNTIME=1 -I include/ -o arprec.js arprec_js.cc src/.libs/libarprec.a
//
// Run:
// node arprec.js
//
// Minify:
// closure-compiler -O ADVANCED arprec.js > arprec.min.js


//using namespace std;
using namespace emscripten;

void
set_precision(int precision)
{
  mp::mp_finalize();
  mp::mp_init(precision+2);
  mp::mpsetoutputprec(precision);
  std::setprecision(precision);
}

std::string
mp_to_string(mp_real& x)
{
  return x.to_string();
}


inline void
add(mp_real& res, const mp_real& x, const mp_real& y)
{
  mp_real::mpadd(x, y, res, mp::prec_words);
}

inline void
add_d(mp_real& res, const mp_real& x, double d)
{
  mp_real::mpadd(x, d, res, mp::prec_words);
}

inline void
sub(mp_real& res, const mp_real& x, const mp_real& y)
{
  mp_real::mpsub(x, y, res, mp::prec_words);
}

inline void
sub_d(mp_real& res, const mp_real& x, double d)
{
  mp_real::mpsub(x, d, res, mp::prec_words);
}

inline void
mul(mp_real& res, const mp_real& x, const mp_real& y)
{
  mp_real::mpmulx(x, y, res, mp::prec_words);
}

inline void
mul_d(mp_real& res, const mp_real& x, double d, int exponent)
{
  mp_real::mpmuld(x, d, exponent, res, mp::prec_words);
}

inline void
divide(mp_real& res, const mp_real& x, const mp_real& y)
{
  mp_real::mpdivx(x, y, res, mp::prec_words);
}

inline void
div_d(mp_real& res, const mp_real& x, double d, int exponent)
{
  mp_real::mpdivd(x, d, exponent, res, mp::prec_words);
}

inline void
square(mp_real& res, const mp_real& x)
{
  mp_real::mpsqx(x, res, mp::prec_words);
}

inline void
square_root(mp_real& res, const mp_real& x)
{
  mp_real::mpsqrtx(x, res, mp::prec_words);
}

inline void
exponential(mp_real& res, const mp_real& x)
{
  mp_real::mpexpx(x, mp_real::_pi, mp_real::_log2, res);
}

inline void
logarithm(mp_real& res, const mp_real& x)
{
  mp_real::mplogx(x, mp_real::_pi, mp_real::_log2, res, mp::prec_words);
}

inline void
logarithm10(mp_real& res, const mp_real& x)
{
  int prec_words = mp::prec_words;
  mp_real::mplogx(x, mp_real::_pi, mp_real::_log2, res, prec_words);
  mp_real::mpdivx(res, mp_real::_log10, res, prec_words);
}

inline void
sine(mp_real& res, const mp_real& x)
{
  mp_real junk;
  mp_real::mpcssx(x, mp_real::_pi, junk, res);
}

inline void
cosine(mp_real& res, const mp_real& x)
{
  mp_real junk;
  mp_real::mpcssx(x, mp_real::_pi, res, junk);
}

inline void
sinecosine(mp_real& s, mp_real& c, const mp_real& x)
{
  mp_real::mpcssx(x, mp_real::_pi, c, s);
}

inline void
tangent(mp_real& res, const mp_real& x)
{
  mp_real c;
  mp_real::mpcssx(x, mp_real::_pi, c, res);
  mp_real::mpdivx(res, c, res, mp::prec_words);
}

inline void
other_cathetus(mp_real& res, const mp_real& x)
{
  mp_real x2, diff, f(1.0, 6);
  int prec_words = mp::prec_words;
  // compute sqrt(1- x^2);
  mp_real::mpmulx(x, x, x2, prec_words);
  mp_real::mpsub(f, x2, diff, prec_words);
  mp_real::mpsqrtx(diff, res, prec_words);
}

inline void
arcsine(mp_real& res, const mp_real& x)
{
  mp_real a;
  other_cathetus(a, x);
  mp_real::mpangx(a, x, mp_real::_pi, res);
}

inline void
arccosine(mp_real& res, const mp_real& x)
{
  mp_real a;
  other_cathetus(a, x);
  mp_real::mpangx(x, a, mp_real::_pi, res);
}

inline void
arctan(mp_real& res, const mp_real& x)
{
  mp_real::mpangx(mp_real(1.0), x, mp_real::_pi, res);
}

inline void
arctan2(mp_real& res, const mp_real& y, const mp_real& x)
{
  mp_real::mpangx(x, y, mp_real::_pi, res);
}

inline int
compare(const mp_real& x, const mp_real& y)
{
  return mp_real::mpcpr(x, y);
}

inline double
to_double(const mp_real& x)
{
  return dble(x);
}





EMSCRIPTEN_BINDINGS(mp_real_class) {
  class_<mp_real>("mp_real")
    .constructor<std::string>()
    .function("index", select_overload<double(int)const> (&mp_real::operator[]))
    ;
}

EMSCRIPTEN_BINDINGS(mp_real_functions) {
  function("to_string", &mp_to_string);
  function("set_precision", &set_precision);
  function("add", &add);
  function("add_d", &add_d);
  function("sub", &sub);
  function("sub_d", &sub_d);
  function("mul", &mul);
  function("mul_d", &mul_d);
  function("div", &divide);
  function("div_d", &div_d);
  function("sqr", &square);
  function("sqrt", &square_root);
  function("exp", &exponential);
  function("log", &logarithm);
  function("log10", &logarithm10);
  function("sin", &sine);
  function("cos", &cosine);
  function("sincos", &sinecosine);
  function("tan", &tangent);
  function("asin", &arcsine);
  function("acos", &arccosine);
  function("atan", &arctan);
  function("atan2", &arctan2);
  function("compare", &compare);
  function("to_double", &to_double);
}

int main(int argc, char **argv)
{
  set_precision(100);
  std::string s("0.1e-2");
  mp_real bsqrt2, bsqrt, bb("1e-1"), z(2.0);

  bsqrt = sqrt(bb);
  bsqrt2 = bsqrt*bsqrt;
  std::cout << " 2.0 = " << mp_real("2.0e0") << std::endl;
  std::cout << " 0.1 = " << bb << std::endl;
  std::cout << " sqrt(0.1) = " << bsqrt << std::endl;
  std::cout << " sqrt(0.1)^2 = " << bsqrt2 << std::endl;

  mp_real a(2), b(2.1), c(2.2);
  std::cout << (a < b) << std::endl;
  std::cout << (b < c) << std::endl;


  mp::mp_finalize();
  return 0;
}
