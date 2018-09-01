package Crypto;


// general interface for the cryptogrpahical primitives
// for simple interfaces: keys are strings, signature is byte stream
public interface CryptoInterface extends HashFunctionInterface, AsymmetricCryptoInterface, SymmetricCryptoInterface, HyerarchicKeyInterface, RandomInterface {
		
		
}
