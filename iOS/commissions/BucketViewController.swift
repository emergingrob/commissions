//
//  BucketViewController.swift
//  commissions
//
//  Created by Rob Miller on 2/18/20.
//  Copyright © 2020 Rob Miller. All rights reserved.
//

import Foundation
import AWSMobileClient
import UIKit

class BucketViewController: UIViewController {

    @IBOutlet weak var accessoryBucket: UILabel!

    @IBOutlet weak var accessoryRevTextField: UITextField!
    
    @IBAction func accessoryRevEdited(_ sender: UITextField) {
            print("textField: \(accessoryRevTextField.text!)")
        let accessory_bucket_value = (accessoryRevTextField.text as! NSString).floatValue * 0.35
        print("bucket value: \(accessory_bucket_value)")
        //let s = NSString(format: "%.2f", accessory_bucket_value)
        accessoryBucket.text = "$ \(accessory_bucket_value)"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("BucketViewController loaded")
    }
}
