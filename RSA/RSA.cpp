//
// Created by ������ on 2019/12/01.
//
#include <iostream>
#include <math.h>
#include <cstdlib>
#include <ctime>
#include <tuple>
#include <vector>

using namespace std;
#define RandomMin 16384
#define RandomMax 32768
#define ull unsigned long long

ull p, q, N, phi, e, d;
ull input;
ull ciperText;
ull decryptedCiperText;


//�з� ��� �׽�Ʈ�� ���� �迭
vector<ull> aList = { 2, 3, 5, 7, 11,
13, 17, 19, 23, 29,
31, 37, 41, 43, 47,
53, 59, 61, 67, 71 };


//calculate x^y % m 
//(Square and multiply algorithm for modular exponentiation)
ull powMod(ull x, ull y, ull m) {
	x %= m;
	ull r = 1ULL;
	while (y > 0) {
		if (y % 2 == 1)
			r = (r * x) % m;
		x = (x * x) % m;
		y /= 2;
	}
	return r;
}

//Miller-Rabin Primarlity test 
inline bool milerRabinPrimality(ull n, ull a) {
	ull d = n - 1;
	while (d % 2 == 0) {
		if (powMod(a, d, n) == n - 1)
			return true;
		d /= 2;
	}
	ull tmp = powMod(a, d, n);
	return tmp == n - 1 || tmp == 1;
}

//�з���� �Ҽ��Ǻ����� �̿��� �Ҽ� �Ǻ�
//20ȸ (�迭�� ũ��) ����
bool isPrime(ull n) {
	if (n <= 1) return false;
	if (n <= 10000ULL) {
		for (ull i = 2; i * i <= n; i++)
			if (n % i == 0)
				return false;
		return true;
	}
	for (ull a : aList)
		if (!milerRabinPrimality(n, a))
			return false;
	return true;
}

//Extended Euclidean Algorithm 
//g (�ִ�����) , x, y�� ����
tuple<int, int, int> extendedEuclideanAlgorithm(int a, int b) {
	if (b == 0) return make_tuple(a, 1, 0);
	int g, x, y;
	tie(g, x, y) = extendedEuclideanAlgorithm(b, a % b);
	return make_tuple(g, y, x - (a / b) * y);
}

void init() {
	srand((unsigned int)time(NULL)); //random number�� ����

	//������ �����ϴ� P ����
	while (true) {
		p = rand() % RandomMax;
		if (isPrime(p) && p >= RandomMin) break;
	}

	//������ �����ϴ� Q ����
	while (true) {
		q = rand() % RandomMax;
		if (p != q && isPrime(q) && q >= RandomMin) break;
	}

	//N�� phi ���
	N = p * q;
	phi = (p - 1) * (q - 1);


	//Ȯ�� ��Ŭ���� �˰����� �̿��Ͽ� e���
	while (true) {
		e = rand() % phi;

		tuple<int, int, int> res;
		res = extendedEuclideanAlgorithm(phi, e);
		//phi �� GCD�� 1�̰�, 0�� �ƴ� e ������ while�� ����
		if (get<0>(res) == 1 && e != 0) break;
	}

	//������ �����ϴ� d key����
	for (d = 1;; d++) {
		if (((d * e) % phi) == 1)
			break;
	}

	cout << "p = " << p << "\n";
	cout << "q = " << q << "\n";
	cout << "N = " << N << "\n";
	cout << "phi = " << phi << "\n";
	cout << "e = " << e << "\n";
	cout << "d = " << d << "\n";
}

//encrpytion
void encrpytion() {
	cout << "**Encryption\n";
	ciperText = powMod(input, e, N);
	cout << "ciper = " << ciperText << "\n";

}


//decrpytion using by Chinese Remainder Theorem
void decryption() {
	cout << "**Decryption\n";
	ull dp = d % (p - 1);
	ull dq = d % (q - 1);
	ull qInv;
	for (qInv = 1;; qInv++) if (((qInv * q) % p) == 1)	break;


	ull m1 = powMod(ciperText, dp, p);
	ull m2 = powMod(ciperText, dq, q);

	ull h = (qInv * (m1 - m2)) % p;
	decryptedCiperText = m2 + h * q;

	cout << "decrypted ciper : " << decryptedCiperText << "\n";
}

int main() {
	init();

	cout << "Message Input :  ";
	cin >> input;
	cout << "Message = " << input << "\n";

	encrpytion();
	decryption();
	cout << "\n\n";
	system("pause");
	return 0;
}